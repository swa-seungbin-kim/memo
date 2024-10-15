package memo.evaluate;

import javax.inject.Named;

import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.di.annotations.Evaluate;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.IStructuredSelection;

public class ProjectOrEmptySelectionExpression {

	@Evaluate
	public boolean isVisible(@Named(IServiceConstants.ACTIVE_SELECTION) IStructuredSelection selection) {
		
		// 1개의 프로젝트에 대해서만 우클릭 하였을 때
		if (selection.size() == 1) {
			return selection.getFirstElement() instanceof IProject;
		}
		
		return false;
	}
	
}
