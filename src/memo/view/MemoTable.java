package memo.view;

import static memo.common.EventTopic.*;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import memo.entity.MemoData;

public class MemoTable extends TableViewer {

	private List<MemoData> modelList;
	
	
	public MemoTable(Composite parent) {
		super(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
	}

	
	@Inject
	@Optional
	public void handleMemoListShow(@UIEventTopic(MEMO_LIST_SHOW) List<MemoData> memoList) {
		
		this.modelList = memoList;
		this.setInput(memoList);
	}
	
	
	@Inject
	@Optional
	public void handleMemoRemove(@UIEventTopic(MEMO_REMOVE) Long id) {
		
		modelList.removeIf(memo -> memo.getId().equals(id));
		this.refresh();
	}
	
	
	@Inject
	@Optional
	public void handleMemoRemove(@UIEventTopic(MEMO_UPDATE) MemoData memoData) {
		
		modelList.stream()
					.filter(memo -> memo.getId().equals(memoData.getId()))
					.forEach(memo -> memo.setContent(memoData.getContent()));
		this.refresh();
	}
	
}
