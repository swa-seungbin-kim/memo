package memo.view;

import java.util.Arrays;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;

import memo.common.EventTopic;
import memo.job.MemoListFetchJob;
import memo.provider.MemoDataContentLabelProvider;
import memo.provider.MemoDataCreatedAtLabelProvider;
import memo.provider.MemoDataModifiedAtLabelProvider;
import memo.provider.MemoDataProjectNameLabelProvider;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.ArrayContentProvider;

public class MemoTableView {
	
	public static final String PART_ID = "memo.memotable";
	
	private MemoTable tableViewer;
	
	private Combo projectSearchCombo;
	
	private Text contentSearchText;
	
	@Inject
	private IEclipseContext context;
	
	@Inject
	private ESelectionService selectionService;
	
	@Inject
	private Provider<MemoListFetchJob> memoListFetchJobProvider;
	

	@PostConstruct
	public void createPartControl(Composite parent, EMenuService menuService) {
		
		// 그리드 레이아웃 설정
		parent.setLayout(new GridLayout(1, false));
		
		// 검색 composite
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(2, false));
		
		// 검색 composite | 프로젝트명 라벨과 콤보박스
		Label projectNameLabel = new Label(composite, SWT.CENTER);
		projectNameLabel.setAlignment(SWT.CENTER);
		projectNameLabel.setText("Project Name");
		// 존재하는 프로젝트 목록 설정
		projectSearchCombo = new Combo(composite, SWT.NONE);
		projectSearchCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		projectSearchCombo.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {}
			
			@Override
			public void focusGained(FocusEvent e) {
				
				IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
				String[] items = Arrays.stream(projects)
										.map(IProject::getName)
										.toArray(String[]::new);
				
				projectSearchCombo.setItems(items);
			}
		});
		// 프로젝트 선택시 DB조회 이벤트 등록
		projectSearchCombo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				
				fetech();
			}
			
		});
		
		// 검색 composite | 내용 라벨과 입력박스
		Label contentLabel = new Label(composite, SWT.NONE);
		contentLabel.setAlignment(SWT.CENTER);
		contentLabel.setText("Content");
		
		contentSearchText = new Text(composite, SWT.BORDER);
		contentSearchText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		contentSearchText.addTraverseListener(e -> {
			
			// 입력박스에서 엔터치면 조회
			if (e.detail == SWT.TRAVERSE_RETURN) {
				fetech();
			}
		});
		
		// 검색결과 출력 테이블 뷰어
		tableViewer = new MemoTable(parent);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		
		new MemoDataProjectNameLabelProvider().addColumnTo(tableViewer);
		new MemoDataContentLabelProvider().addColumnTo(tableViewer);
		new MemoDataCreatedAtLabelProvider().addColumnTo(tableViewer);
		new MemoDataModifiedAtLabelProvider().addColumnTo(tableViewer);
		
		Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		menuService.registerContextMenu(table, "memo.popupmenu");
		
		// 우클릭 팝업메뉴 클릭시 선택 대상 전달
		tableViewer.addSelectionChangedListener(e -> {
			selectionService.setSelection(e.getSelection());
		});
		
		ContextInjectionFactory.inject(tableViewer, context);
	}
	
	
	@Inject
	@Optional
	public void handleEditorSearch(@UIEventTopic(EventTopic.EDITOR_SEARCH) String data) {
		
		String[] projectNameAndText = data.split(":");
		
		String projectName = projectNameAndText[0];
		String text = projectNameAndText[1];
		
		String[] projects = projectSearchCombo.getItems();
		
		for (int index = 0; index < projects.length; index++) {
			
			if (Objects.equals(projectName, projects[index])) {
				projectSearchCombo.select(index);
				contentSearchText.setText(text);
				fetech();
				return;
			}
			
		}
	}
	
	
	private void fetech() {
		
		int index = projectSearchCombo.getSelectionIndex();
		if (index < 0) {
			return;
		}
		
		String projectName = projectSearchCombo.getItem(index);
		
		MemoListFetchJob job = memoListFetchJobProvider.get();
		job.setParameter(projectName, contentSearchText.getText());
		job.schedule();
	}
	
	
	@Focus
	public void setFocus() {
		projectSearchCombo.setFocus();
	}
	
	
	@PreDestroy
	public void dispose() {
		
		ContextInjectionFactory.uninject(tableViewer, context);
	}
}
