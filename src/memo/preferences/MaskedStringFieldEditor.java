package memo.preferences;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class MaskedStringFieldEditor extends StringFieldEditor {

	private Text textField;

	
    public MaskedStringFieldEditor(String name, String labelText, Composite parent) {
        super(name, labelText, parent);
    }

    @Override
    protected void doFillIntoGrid(Composite parent, int numColumns) {
    	
        super.doFillIntoGrid(parent, numColumns);
        textField = getTextControl();
        textField.setEchoChar('*');
    }

    @Override
    protected Text getTextControl() {
    	
        if (textField == null) {
            textField = super.getTextControl();
        }
        return textField;
    }
    
    
    @Override
    public void dispose() {
    	
    	super.dispose();
    	textField.dispose();
    }
}
