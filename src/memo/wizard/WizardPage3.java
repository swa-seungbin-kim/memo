package memo.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

public class WizardPage3 extends WizardPage {
	
	private Text projectName;
	
	private Text memoInput;

	
	protected WizardPage3() {
		
		super("Confirm");
		setTitle("Confirm");
	}

	@Override
	public void createControl(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NONE);
		setControl(container);
		container.setLayout(new GridLayout(1, false));
		
		Label projectNameLabel = new Label(container, SWT.NONE);
		projectNameLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		projectNameLabel.setText("Project");
		
		projectName = new Text(container, SWT.BORDER);
		projectName.setEditable(false);
		projectName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		Label memoInputLabel = new Label(container, SWT.NONE);
		memoInputLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		memoInputLabel.setText("Memo Input");
		
		memoInput = new Text(container, SWT.BORDER);
		memoInput.setEditable(false);
		memoInput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}
	
	
	@Override
	public void setVisible(boolean visible) {
		
		super.setVisible(visible);
		if (visible) {
			NewMemoWizard wizard = (NewMemoWizard) getWizard();
			
			projectName.setText(wizard.getSelectedProject());
			memoInput.setText(wizard.getMemoInput());
		}
	}
}
