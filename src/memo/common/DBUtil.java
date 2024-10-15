package memo.common;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Properties;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.osgi.service.prefs.Preferences;

import memo.entity.MemoData;
import memo.strategy.SnakeCasePhysicalNamingStrategy;

import static memo.common.PreferenceField.*;

public class DBUtil {

	private static final ClassLoader ORIGINAL_CLASS_LOADER = Thread.currentThread().getContextClassLoader();
	
	private static SessionFactory sessionFactory;
	
	private static Properties databaseSetting = new Properties();
	
	
	static {
		databaseSetting.put(Environment.HBM2DDL_AUTO, "update");
		databaseSetting.put(Environment.SHOW_SQL	, "true");
		databaseSetting.put(Environment.FORMAT_SQL	, "true");
		databaseSetting.put(Environment.POOL_SIZE	, "1");
	}
	
	
	public static boolean isTestConnectionOk(String driverPath, 
											 String driverClassName, 
											 String url, 
											 String username, 
											 String password) {
		
		try {
			// 드라이버 동적 등록
			registerDriver(driverPath, driverClassName);
			
			// 테스트값 설정
			Properties testProperties = new Properties();
			setProperties(testProperties,
						  driverClassName, 
						  url, 
						  username, 
						  password);
			
			// 연결시도 후 끊기
			new Configuration()
					.setProperties(testProperties)
					.buildSessionFactory()
					.close();
			
			return true;
		} catch (Exception e) {
			
			e.printStackTrace();
			return false;
		}
		
	}
	
	
	public static EntityManager getEntityManager() throws Exception {

		if (sessionFactory == null) {
			connectUsingPreference();
		}
		
		return sessionFactory.createEntityManager();
	}
	
	
	public static void closeSessionFactory() {
		
		if (sessionFactory != null) {
			sessionFactory.close();
		}
	}
	
	
	public static void applyDatabasePreference() {
		
		try {
			connectUsingPreference();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private static void connectUsingPreference() throws Exception {
		
		closeSessionFactory();
		
		Preferences preferences = InstanceScope.INSTANCE.getNode("memo.preference");
		
		// 드라이버 동적 등록
		String driverPath = preferences.get(DRIVER_PATH.name(), "");
		String driverClassName = preferences.get(DRIVER_CLASS.name(), "");
		registerDriver(driverPath, driverClassName);
		
		// 연결 기본설정
		setProperties(databaseSetting,
					  driverClassName,
					  preferences.get(CONNECTION_URL.name(), ""),
					  preferences.get(USERNAME.name()	   , ""),
					  preferences.get(PASSWORD.name()	   , ""));
		
		Configuration configuration = new Configuration();
		
		configuration.setPhysicalNamingStrategy(new SnakeCasePhysicalNamingStrategy());
		configuration.addAnnotatedClass(MemoData.class);
		
		// 연결
		sessionFactory = configuration
				.setProperties(databaseSetting)
				.buildSessionFactory();
		
		Thread.currentThread().setContextClassLoader(ORIGINAL_CLASS_LOADER);
	}
	
	
	private static void registerDriver(String driverPath, String driverClassName) throws Exception {
		
		// 드라이버 JAR 파일 URL 생성
		File driverFile = new File(driverPath);
		URL driverURL = driverFile.toURI().toURL();
		
		// 드라이버 JAR 파일 로드
		ClassLoader classLoader = new URLClassLoader(new URL[] {driverURL});
		
		// 하이버네이트가 동적 로딩한 드라이버를 찾을 수 있도록 처리
		Thread.currentThread().setContextClassLoader(classLoader);
		
		// 드라이버 클래스 로드
		Class<?> driverClass = Class.forName(driverClassName, true, classLoader);
		
		// 드라이버 인스턴스 생성
		Driver driver = (Driver) driverClass.getDeclaredConstructor()
											.newInstance();
		
		// 드라이버 등록
		DriverManager.registerDriver(driver);
		DriverManager.setLoginTimeout(5);
	}
	
	
	private static void setProperties(Properties target, 
									  String driverClassName,  
									  String url, 
									  String username, 
									  String password) {
		
		target.put(Environment.DRIVER, driverClassName);
		target.put(Environment.URL	 , url);
		target.put(Environment.USER  , username);
		target.put(Environment.PASS  , password);
		
		// JPA 방언 설정
		DatabaseType database = DatabaseType.ofDriverClassName(driverClassName);
		databaseSetting.put(Environment.DIALECT, database.dialect());
	}
	
	
	@PreDestroy
	protected static void preDestroy() {
		
		closeSessionFactory();
	}
}
