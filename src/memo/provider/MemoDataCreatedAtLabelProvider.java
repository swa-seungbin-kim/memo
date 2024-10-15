package memo.provider;

import static memo.common.StringFormat.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import memo.entity.MemoData;

public class MemoDataCreatedAtLabelProvider extends MemoDataColumnLabelProvider {

	@Override
	public String getText(Object element) {
		
		if (element instanceof MemoData memoData) {
			LocalDateTime createdAt = memoData.getCreatedAt();
			return DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT).format(createdAt);
		}
		
		return "";
	}

	@Override
	public String getTitle() {
		
		return "Created At";
	}

	@Override
	public int getWidth() {
		
		return 120;
	}

}
