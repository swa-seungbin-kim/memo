package memo.wizard;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.FillLayout;

public class WizardPage2 extends WizardPage {
	
	private Text text;
	

	protected WizardPage2() {
		
		super("Memo Input");
		setTitle("Memo Input(Optional)");
	}

	@Override
	public void createControl(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NONE);
		setControl(container);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		text = new Text(container, SWT.BORDER);
	}

	
	@Override
	public IWizardPage getPreviousPage() {
		
		updateText();
		return super.getPreviousPage();
	}
	
	
	@Override
	public IWizardPage getNextPage() {

		updateText();
		return super.getNextPage();
	}
	
	
	private void updateText() {
		
		NewMemoWizard wizard = (NewMemoWizard) getWizard();
		wizard.setMemoInput(text.getText());
	}
	
}
