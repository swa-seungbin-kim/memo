package memo.common;

import java.util.Arrays;

public enum DatabaseType {
	
	EMPTY(
		"",
		"",
		"",
		""
	),
	
	MY_SQL(
		"MySQL",
		"com.mysql.cj.jdbc.Driver",
		"jdbc:mysql://localhost:3306/plugin",
		"org.hibernate.dialect.MySQLDialect"
	),
	
	POSTGRES(
		"Postgres",
		"org.postgresql.Driver",
		"jdbc:postgresql://localhost:5432/plugin",
		"org.hibernate.dialect.PostgreSQLDialect"
	);
	
	
	private final String displayedName;
	
	private final String defaultDriverClass;
	
	private final String defaultConnectionURL;
	
	private final String dialect;
	
	
	private DatabaseType(String displayedName, String defaultDriverClass, String defaultConnectionURL, String dialect) {
		
		this.displayedName = displayedName;
		this.defaultDriverClass = defaultDriverClass;
		this.defaultConnectionURL = defaultConnectionURL;
		this.dialect = dialect;
	}
	
	
	public static String[][] displayedValues() {
		
		return Arrays.stream(DatabaseType.values())
				.map(DB -> new String[] {DB.displayedName(), DB.name()})
				.toArray(String[][]::new);
	}
	
	
	public String displayedName() {
		
		return displayedName;
	}
	
	
	public String defaultDriverClass() {
		
		return defaultDriverClass;
	}
	
	
	public String defaultConnectionURL() {
		
		return defaultConnectionURL;
	}
	
	
	public String dialect() {
		
		return dialect;
	}
	
	
	public static DatabaseType ofDriverClassName(String driverClassName) {
		
		return Arrays.stream(values())
						.filter(dbType -> driverClassName.contains(dbType.displayedName.toLowerCase()))
						.findAny()
						.orElseGet(() -> DatabaseType.EMPTY);
	}
}
