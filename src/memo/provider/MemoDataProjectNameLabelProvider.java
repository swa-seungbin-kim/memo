package memo.provider;

import memo.entity.MemoData;

public class MemoDataProjectNameLabelProvider extends MemoDataColumnLabelProvider {

	@Override
	public String getText(Object element) {
		
		if (element instanceof MemoData memoData) {
			return memoData.getProjectName();
		}
		
		return "";
	}

	@Override
	public String getTitle() {
		
		return "Project Name";
	}

	@Override
	public int getWidth() {
		
		return 100;
	}

}
