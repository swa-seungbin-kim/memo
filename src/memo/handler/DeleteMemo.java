package memo.handler;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.IStructuredSelection;

import memo.entity.MemoData;
import memo.job.MemoDeleteJob;
import memo.view.MemoView;

public class DeleteMemo {
	
	@Inject
    private EPartService partService;
	
	@Inject
	private Provider<MemoDeleteJob> memoDeleteJobProvider;
	
	
	@Execute
    public void execute(@Optional @Named("memo.command.parameter.delete")
    					String source,
    					ESelectionService selectionService) {
		
		// MemoView 의 툴바에서 삭제처리
		if ("TOOLBAR".equals(source)) {
			
			MPart mPart = partService.findPart(MemoView.PART_ID);
			MemoView memoView = (MemoView) mPart.getObject();
			Long selectedId = memoView.getSelectedId();
			
			deleteMemo(selectedId);
			return;
		}
		
		// MemoTableView 의 우클릭 삭제처리
		if ("TABLE".equals(source)) {
			
			IStructuredSelection selection = (IStructuredSelection) selectionService.getSelection();
			if (selection == null || selection.isEmpty()) {
				return;
			}
			
			MemoData memoData = (MemoData) selection.getFirstElement();
			Long selectedId = memoData.getId();
			
			deleteMemo(selectedId);
			return;
		}
		
    }
	
	
	private void deleteMemo(Long id) {
		
		MemoDeleteJob memoDeleteJob = memoDeleteJobProvider.get();
		memoDeleteJob.setParameter(id);
		memoDeleteJob.schedule();
	}
	
}
