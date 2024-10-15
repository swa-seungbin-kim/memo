package memo.preferences;

import static memo.common.PreferenceField.CONNECTION_URL;
import static memo.common.PreferenceField.DRIVER_CLASS;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;

import memo.common.DatabaseType;

public class CustomComboFieldEditor extends ComboFieldEditor {

	private List<StringFieldEditor> stringFieldEditors = new ArrayList<>();;
	
	
	public CustomComboFieldEditor(String name, String labelText, String[][] entryNamesAndValues, Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
	}

	
	@Override
	protected void valueChanged(String oldValue, String newValue) {
		
		super.valueChanged(oldValue, newValue);
		
		// DB 콤보 선택시 연관된 필드 기본값을 자동으로 채우기 위함
		stringFieldEditors.stream()
						.forEach(sfe -> updateRelatedStringFieldEditor(sfe, newValue));
	}
	
	
	public void addRelatedStringFieldEditor(StringFieldEditor stringFieldEditor) {
		
		this.stringFieldEditors.add(stringFieldEditor);
	}

	
	private void updateRelatedStringFieldEditor(StringFieldEditor stringFieldEditor, String value) {
		
		DatabaseType database;
		
		try {
			database = DatabaseType.valueOf(value);
		} catch (Exception ignore) {
			return;
		}
		
		// 콤보 선택과 관련된 다른 설정값을 같이 변경
		String preferenceName = stringFieldEditor.getPreferenceName();
		
		if (DRIVER_CLASS.name().equals(preferenceName)) {
			stringFieldEditor.setStringValue(database.defaultDriverClass());
			return;
		}
		if (CONNECTION_URL.name().equals(preferenceName)) {
			stringFieldEditor.setStringValue(database.defaultConnectionURL());
			return;
		}
		
	}


	@Override
	public void dispose() {
		
		super.dispose();
		stringFieldEditors = null;
	}
	
	
}
