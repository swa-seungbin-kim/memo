package memo.job;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;

import memo.common.EventTopic;
import memo.entity.MemoData;

@Creatable
public class MemoUpdateJob extends TransactionJobTemplate {

	@Inject
	private IEventBroker eventBroker;
	
	private Long id;
	
	private String content;
	
	
	public MemoUpdateJob() {
		super("Memo Update JOB");
	}
	
	
	public void setParameter(Long id, String content) {
		
		this.id = id;
		this.content = content;
	}
	

	@Override
	void process() {
		
		MemoData memoData = em.find(MemoData.class, id);
		
		memoData.setContent(content);
		
		eventBroker.post(EventTopic.MEMO_UPDATE, memoData);
	}

}
