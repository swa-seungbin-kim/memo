package memo.job;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import memo.common.DBUtil;

public abstract class TransactionJobTemplate extends Job {
	
	protected TransactionJobTemplate(String name) {
		super(name);
	}

	protected EntityManager em;

	
	@Override
	final protected IStatus run(IProgressMonitor monitor) {
		
		EntityTransaction transaction = null;
		try {
			// 트랜잭션 시작
			em = DBUtil.getEntityManager();
			transaction = em.getTransaction();
			transaction.begin();
			
			process();
			
			// 트랜잭션 커밋
			transaction.commit();
			return Status.OK_STATUS;
		} catch (Exception e) {
			e.printStackTrace();
			
			// 트랜잭션 롤백
			if (transaction != null) {
				transaction.rollback();
			}
			
			return Status.error(getName() + " Error", e);
		} finally {
			if (em != null) {
				em.close();
			}
		}
		
	}
	
	
	abstract void process();

}
