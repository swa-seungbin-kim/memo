package memo.job;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;

import memo.common.EventTopic;
import memo.entity.MemoData;

@Creatable
public class MemoDeleteJob extends TransactionJobTemplate {

	@Inject
	private IEventBroker eventBroker;
	
	private Long id;
	
	
	public MemoDeleteJob() {
		super("Memo Delete JOB");
	}
	
	
	public void setParameter(Long id) {
		
		this.id = id;
	}
	

	@Override
	void process() {
		
		MemoData memoData = em.find(MemoData.class, id);
		em.remove(memoData);
		
		eventBroker.post(EventTopic.MEMO_REMOVE, id);
	}

}
