package memo.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.texteditor.ITextEditor;

import memo.common.EventTopic;
import memo.view.MemoTableView;

public class EditorWordSearch extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		// 1. 에디터 내 선택된 문자 가져오기
		String text = getSelectedText(event);
		if (text == null || text.isBlank()) {
			return null;
		}
		
		// 2. 현재 프로젝트 이름 얻어오기
		String projectName = getCurrentProjectName(event);
		if (text == null || text.isBlank()) {
			return null;
		}
		
		// 3. 뷰 열기
		openMemoTableView();
		
		// 4. 이벤트 발행
		IWorkbench workbench = PlatformUI.getWorkbench();
		IEventBroker eventBroker = ((Workbench) workbench).getApplication()
				.getContext()
				.get(IEventBroker.class);
		
		eventBroker.post(EventTopic.EDITOR_SEARCH, projectName + ":" + text);
		
		return null;
	}
	
	
	private String getSelectedText(ExecutionEvent event) {
		
		IEditorPart activeEditor = HandlerUtil.getActiveEditor(event);
		
		ISelection selection = activeEditor.getSite()
											.getSelectionProvider()
											.getSelection();
		
		if (selection instanceof TextSelection textSelection) {
			return textSelection.getText();
		}
		
		return null;
	}
	
	
	private String getCurrentProjectName(ExecutionEvent event) throws ExecutionException {
		
		IEditorPart activeEditor = HandlerUtil.getActiveEditor(event);
		
		if (activeEditor instanceof ITextEditor textEditor) {
			
			IEditorInput editorInput = textEditor.getEditorInput();
			IFile iFile = editorInput.getAdapter(IFile.class);
			
			return iFile.getProject().getName();
		}
		
		return null;
	}
	
	
	private void openMemoTableView() {
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		
		try {
			page.showView(MemoTableView.PART_ID);
			
		} catch (PartInitException e) {
			e.printStackTrace();
			MessageDialog.openError(window.getShell(), "Error", "View Open Exception: " + e.getMessage());
		}
	}
	
}
