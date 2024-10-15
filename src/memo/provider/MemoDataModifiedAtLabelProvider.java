package memo.provider;

import static memo.common.StringFormat.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import memo.entity.MemoData;

public class MemoDataModifiedAtLabelProvider extends MemoDataColumnLabelProvider {

	@Override
	public String getText(Object element) {
		
		if (element instanceof MemoData memoData) {
			LocalDateTime modifiedAt = memoData.getModifiedAt();
			return DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT).format(modifiedAt);
		}
		
		return "";
	}

	@Override
	public String getTitle() {
		
		return "Modified At";
	}

	@Override
	public int getWidth() {
		
		return 120;
	}

}
