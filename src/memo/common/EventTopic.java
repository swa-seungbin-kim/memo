package memo.common;

import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;

@Creatable
@Singleton
public final class EventTopic {
	
	public static final String FONT_PREFERENCE_APPLY = "memo/preference/font/apply";
	public static final String DATABASE_PREFERENCE_APPLY = "memo/preference/database/apply";
	public static final String MEMO_LIST_SHOW = "memo/list/show";
	public static final String MEMO_REMOVE = "memo/remove";
	public static final String MEMO_OPEN = "memo/open";
	public static final String MEMO_UPDATE = "memo/update";
	public static final String EDITOR_SEARCH = "memo/editor";
	
	private EventTopic() {}
	
}
