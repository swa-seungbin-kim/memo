package memo.handler;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.ui.dialogs.PreferencesUtil;

public class OpenPreferences {

	@Execute
    public void execute() {
		
		// 플러그인 설정페이지 열기
		PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(
        		null,
        		"memo.preferencepage",
        		new String[] {"memo.preferencepage"},
        		null);
		
        dialog.open();
    }
	
}
