package memo.job;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;

import memo.common.EventTopic;
import memo.entity.MemoData;

@Creatable
public class MemoListFetchJob extends TransactionJobTemplate {

	@Inject
	private IEventBroker eventBroker;
	
	private String projectName;
	
	private String content;
	
	
	public MemoListFetchJob() {
		super("Memo List Fetch JOB");
	}
	
	
	public void setParameter(String projectName, String content) {
		
		this.projectName = projectName;
		this.content = content;
	}
	

	@Override
	void process() {
		
		List<MemoData> resultList = em.createQuery("""
				SELECT m 
				FROM   MemoData m 
				WHERE  m.projectName = :projectName 
				AND	   m.content LIKE CONCAT('%', :content, '%')
				ORDER BY m.id DESC
				""",
				MemoData.class)
			.setParameter("projectName", projectName)
			.setParameter("content", content)
			.getResultList();
		
		eventBroker.post(EventTopic.MEMO_LIST_SHOW, resultList);
	}

}
