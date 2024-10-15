package memo.provider;

import memo.entity.MemoData;

public class MemoDataContentLabelProvider extends MemoDataColumnLabelProvider {

	@Override
	public String getText(Object element) {
		
		if (element instanceof MemoData memoData) {
			return memoData.getContent();
		}
		
		return "";
	}

	@Override
	public String getTitle() {
		
		return "Content";
	}

	@Override
	public int getWidth() {
		
		return 300;
	}

}
