package memo.view;

import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.osgi.service.prefs.BackingStoreException;

import memo.entity.MemoData;

import org.eclipse.swt.custom.CTabItem;

import static memo.common.EventTopic.*;

public class MemoView {
	
	public static final String PART_ID = "memo.memoview";
	
	private CTabFolder tabFolder;
	
	@Inject
	private IEclipseContext context;
	
	
	@PostConstruct
	public void createPartControl(Composite parent, EMenuService menuService) throws BackingStoreException {
		
		menuService.registerContextMenu(parent, "memo.toolbar");
		
		tabFolder = new CTabFolder(parent, SWT.BORDER);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	}
	
	
	public Long getSelectedId() {
		
		CTabItem selection = tabFolder.getSelection();
		if (selection == null) {
			return null;
		}
		Memo memo = Memo.class.cast(selection.getControl());
		return memo.getId();
	}
	
	
	@Inject
	@Optional
	public void handleMemoOpen(@UIEventTopic(MEMO_OPEN) MemoData memoData) {
		
		java.util.Optional<CTabItem> findedTab = findTab(memoData.getId());
		
		if (findedTab.isPresent()) {
			tabFolder.setSelection(findedTab.get());
			return;
		}
		
		CTabItem newItem = new CTabItem(tabFolder, SWT.CLOSE);
		newItem.setText(memoData.getProjectName() + "-" + memoData.getId());
		
		Memo text = new Memo(memoData.getId(), tabFolder, context);
		text.setText(memoData.getContent());
		
		newItem.setControl(text);
		tabFolder.setSelection(newItem);
	}
	
	
	@Inject
	@Optional
	public void handleMemoRemove(@UIEventTopic(MEMO_REMOVE) Long id) {
		
		findTab(id).ifPresent(CTabItem::dispose);
	}
	

	@Focus
	public void setFocus() {
		
	}
	
	
	@PreDestroy
	public void dispose() {
		
		tabFolder.dispose();
	}
	
	
	private java.util.Optional<CTabItem> findTab(Long id) {
		
		return Arrays.stream(tabFolder.getItems())
				.filter(tabItem -> {
					Memo memo = Memo.class.cast(tabItem.getControl());
					return memo.matchesMemoId(id);
				})
				.findAny();
	}

}
