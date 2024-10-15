package memo.handler;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import memo.common.EventTopic;
import memo.entity.MemoData;
import memo.view.MemoView;

public class OpenMemo {

	@Inject
    private IEventBroker eventBroker;
	
	
	@Execute
    public void execute(ESelectionService selectionService) {
		
		// MemoTableView 에서의 메모 우클릭 -> open
		IStructuredSelection selection = (IStructuredSelection) selectionService.getSelection();
		if (selection == null || selection.isEmpty()) {
			return;
		}
		
		MemoData memoData = (MemoData) selection.getFirstElement();
		
		openMemoView();
		eventBroker.post(EventTopic.MEMO_OPEN, memoData);
    }
	
	
	private void openMemoView() {
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		
		try {
			page.showView(MemoView.PART_ID);
			
		} catch (PartInitException e) {
			e.printStackTrace();
			MessageDialog.openError(window.getShell(), "Error", "View Open Exception: " + e.getMessage());
		}
	}
}
