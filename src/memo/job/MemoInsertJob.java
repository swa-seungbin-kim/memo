package memo.job;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;

import memo.common.EventTopic;
import memo.entity.MemoData;

@Creatable
public class MemoInsertJob extends TransactionJobTemplate {

	@Inject
	private IEventBroker eventBroker;
	
	private String projectName;
	
	private String content;
	
	
	public MemoInsertJob() {
		super("Memo Insert JOB");
	}
	
	
	public void setParameter(String projectName, String content) {
		
		this.projectName = projectName;
		this.content = content;
	}
	

	@Override
	void process() {
		
		MemoData memoData = new MemoData();
		memoData.setProjectName(projectName);
		memoData.setContent(content);
		
		em.persist(memoData);
		
		eventBroker.post(EventTopic.MEMO_OPEN, memoData);
	}

}
