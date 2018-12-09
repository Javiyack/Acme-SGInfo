
package repositories;

import domain.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
    
    @Query("select r from Request r where r.responsible.id=?1")
    Collection<Request> findByResponsibleId(int id);

    @Query("select r from Request r where r.status = 'ACCEPTED'")
    Collection<Request> findAllAcceptedRequests();

    @Query("select r from Request r where r.servant.id=?1")
    Collection<Request> findAllRequestsByServantId(int id);

    @Query("select r from Request r where r.servant.id=?1 and r.startingDay is not null and r.endingDay is not null and r.status = 'ACCEPTED'")
    Collection<Request> findActiveRequestsByServantId(int servantId);
}

