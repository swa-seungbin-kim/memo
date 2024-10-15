package memo.handler;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import memo.wizard.NewMemoWizard;

public class NewMemo {

	@Inject
	private ESelectionService selectionService;
	
	@Inject
	private IEclipseContext context;
	
	
	@Execute
    public void execute(Shell shell) {
		
		// 선택된 프로젝트 이름을 기본값으로 신규메모 위자드를 띄운다.
		Object selection = selectionService.getSelection();
		
		if (selection instanceof IStructuredSelection structuredSelection) {
			Object firstElement = structuredSelection.getFirstElement();
			
			if (firstElement instanceof IProject project) {
				openWizard(shell, project.getName());
			}
		} else {
			openWizard(shell, "");
		}
		
    }
	
	
	private void openWizard(Shell shell, String projectName) {
		
		WizardDialog wizardDialog = new WizardDialog(shell, new NewMemoWizard(context, projectName));
        wizardDialog.open();
	}
	
}
