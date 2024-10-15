package memo.wizard;

import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import memo.job.MemoInsertJob;
import memo.view.MemoView;

public class NewMemoWizard extends Wizard implements INewWizard {

	private WizardPage1 page1;
	private WizardPage2 page2;
	private WizardPage3 page3;
	
	private String selectedProject;
	private String memoInput;
	
	private IEclipseContext context;
	
	@Inject
	private Provider<MemoInsertJob> memoInsertJobProvider;
	
	
	public NewMemoWizard(IEclipseContext context, String selectedProject) {
		super();
		
		this.context = context;
		this.selectedProject = selectedProject;
		
		ContextInjectionFactory.inject(this, context);
	}
	

	public String getSelectedProject() {
		return selectedProject;
	}


	public void setSelectedProject(String selectedProject) {
		this.selectedProject = selectedProject;
	}


	public String getMemoInput() {
		return memoInput;
	}


	public void setMemoInput(String memoInput) {
		this.memoInput = memoInput;
	}


	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	@Override
	public boolean performFinish() {
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		
		try {
			page.showView(MemoView.PART_ID);
		} catch (PartInitException e) {
			e.printStackTrace();
			MessageDialog.openError(window.getShell(), "Error", "뷰 열기 실패: " + e.getMessage());
		}
		
		MemoInsertJob memoInsertJob = memoInsertJobProvider.get();
		memoInsertJob.setParameter(selectedProject, memoInput);
		memoInsertJob.schedule();
		return true;
	}

	
	@Override
	public void addPages() {
		page1 = new WizardPage1();
		page2 = new WizardPage2();
		page3 = new WizardPage3();
		addPage(page1);
        addPage(page2);
        addPage(page3);
	}

	
	@Override
	public boolean canFinish() {
		
		IWizardPage currentPage = getContainer().getCurrentPage();
		return super.canFinish() && currentPage == page3;
	}

	
	@Override
	public void dispose() {
		
		ContextInjectionFactory.uninject(this, context);
		super.dispose();
	}
	
}
