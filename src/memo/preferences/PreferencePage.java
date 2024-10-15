package memo.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.FontFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import memo.common.DBUtil;
import memo.common.DatabaseType;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;

import javax.inject.Inject;

import org.eclipse.jface.preference.ColorFieldEditor;

import static memo.common.EventTopic.*;
import static memo.common.PreferenceField.*;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	@Inject
	private IEventBroker eventBroker;
	
	
	public PreferencePage() {
		super(GRID);
	}
	
	@Override
	public void init(IWorkbench workbench) {
		
		setPreferenceStore(new ScopedPreferenceStore(InstanceScope.INSTANCE, "memo.preference"));		
		
		ContextInjectionFactory.inject(this, workbench.getService(IEclipseContext.class));
	}

	@Override
	protected void createFieldEditors() {
		
		// 폰트 및 폰트색상
		addField(new FontFieldEditor(FONT.name(), FONT.text(), null, getFieldEditorParent()));
		addField(new ColorFieldEditor(FONT_COLOR.name(), FONT_COLOR.text(), getFieldEditorParent()));
		
		// DB 콤보 선택
		CustomComboFieldEditor databaseCombo = new CustomComboFieldEditor(DATABASE.name(), DATABASE.text(), DatabaseType.displayedValues(), getFieldEditorParent());
		addField(databaseCombo);
		
		// 드라이버 파일 선택
		FileFieldEditor driverFile = new FileFieldEditor(DRIVER_PATH.name(), DRIVER_PATH.text(), getFieldEditorParent());
		driverFile.setFileExtensions(new String[] {"*.jar"});
		addField(driverFile);
		
		// 드라이버 클래스 입력
		StringFieldEditor driverClass = new StringFieldEditor(DRIVER_CLASS.name(), DRIVER_CLASS.text(), getFieldEditorParent());
		addField(driverClass);
		databaseCombo.addRelatedStringFieldEditor(driverClass);
		
		// 연결 URL 입력
		StringFieldEditor connectionUrl = new StringFieldEditor(CONNECTION_URL.name(), CONNECTION_URL.text(), getFieldEditorParent());
		addField(connectionUrl);
		databaseCombo.addRelatedStringFieldEditor(connectionUrl);
		
		// username 입력
		StringFieldEditor username = new StringFieldEditor(USERNAME.name(), USERNAME.text(), getFieldEditorParent());
		addField(username);
		
		// password 입력
		MaskedStringFieldEditor password = new MaskedStringFieldEditor(PASSWORD.name(), PASSWORD.text(), getFieldEditorParent());
		addField(password);
		
		// DB 연결 테스트버튼
		Button testButton = new Button(getFieldEditorParent(), SWT.PUSH);
		testButton.setText("Connection Test");
		
		// 테스트연결 결과
		Text testResult = new Text(getFieldEditorParent(), SWT.READ_ONLY);
		testResult.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// 테스트연결
		testButton.addSelectionListener(new SelectionListener() {
			
			private int tryCount;
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				testResult.setText("Trying to connect...");
				
				Display.getCurrent().asyncExec(() -> {
					
					boolean testOk = DBUtil.isTestConnectionOk(driverFile.getStringValue(), 
															   driverClass.getStringValue(), 
															   connectionUrl.getStringValue(), 
															   username.getStringValue(), 
															   password.getStringValue());
					
					if (testOk) {
						testResult.setText("success");
					} else {
						String format = "[%s]failed";
						testResult.setText(String.format(format, ++tryCount));
					}
					
				});
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			
		});
		
	}

	
	@Override
	public boolean performOk() {
		
		boolean result = super.performOk();
		apply();
		
		return result;
	}
	

	@Override
	protected void performApply() {
		
		super.performApply();
		apply();
	}
	
	
	private void apply() {
		
		eventBroker.post(FONT_PREFERENCE_APPLY, null);
		DBUtil.applyDatabasePreference();
	}
	
}
