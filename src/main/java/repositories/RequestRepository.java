
package repositories;

import domain.MonthlyDue;
import domain.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

    @Query("select r from Request r where r.responsible.id=?1")
    Collection<Request> findByResponsibleId(int id);

    @Query("select r from Request r where r.responsible.id=?1 and r.servant.id=?2")
    Collection<Request> findByResponsibleAndServant(int responsibleId, int servantId);

    @Query("select r from Request r where r.status = 'ACCEPTED'")
    Collection<Request> findAllAcceptedRequests();

    @Query("select r from Request r where r.servant.id=?1")
    Collection<Request> findAllRequestsByServantId(int id);

    @Query("select r from Request r where r.servant.id=?1 and r.startingDay is not null and r.endingDay is not null and r.status = 'ACCEPTED'")
    Collection<Request> findActiveRequestsByServantId(int servantId);

    @Query("select distinct(r) from Request r where r.status='ACCEPTED' and r.startingDay<?1")
    Collection<Request> findFacturables(Date limit);

    @Query("select distinct(due) from MonthlyDue due where due.request.id=?1")
    Collection<MonthlyDue> findAllMonthlyDues(int requestId);

    @Query("select distinct(r) from Request r where r.status='ACCEPTED' and r.startingDay<?1 and r.responsible.customer.id=?2")
    Collection<Request> findFacturablesByCustomerId(Date limit, int id);
}

