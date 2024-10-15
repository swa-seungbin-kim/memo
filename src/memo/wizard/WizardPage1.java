package memo.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.layout.GridData;

import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;

public class WizardPage1 extends WizardPage {
	
	private Table table;
	

	protected WizardPage1() {
		
		super("Project Select");
		setTitle("Project Select");
	}

	@Override
	public void createControl(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NONE);
		setControl(container);
		container.setLayout(new GridLayout(1, false));
		
		CheckboxTableViewer checkboxTableViewer = CheckboxTableViewer.newCheckList(container, SWT.BORDER | SWT.FULL_SELECTION);
		checkboxTableViewer.setAllGrayed(false);
		checkboxTableViewer.setAllChecked(false);
		checkboxTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		
		// 현재 프로젝트 목록
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		String[] items = Arrays.stream(projects)
								.map(IProject::getName)
								.toArray(String[]::new);
		checkboxTableViewer.setInput(items);
		
		NewMemoWizard wizard = (NewMemoWizard) getWizard();
		
		// 초기 선택값
		boolean chekced = checkboxTableViewer.setChecked(wizard.getSelectedProject(), true);
		if (chekced) {
			setPageComplete(true);
		} else {
			setPageComplete(false);
		}
		
		// 1개만 체크되도록 설정
		checkboxTableViewer.addCheckStateListener(e -> {
			// 체크해재시
			if (!e.getChecked()) {
				wizard.setSelectedProject(null);
				setPageComplete(false);
				return;
			}
			
			// 선택한 체크 외 나머지 체크 해제
			String checked = (String) e.getElement();
			checkboxTableViewer.setAllChecked(false);
			
			checkboxTableViewer.setChecked(checked, true);
			wizard.setSelectedProject(checked);
			setPageComplete(true);
		});
		
		table = checkboxTableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}


}
