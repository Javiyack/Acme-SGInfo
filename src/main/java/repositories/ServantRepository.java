/**
 * 
 */

package repositories;

import domain.Incidence;
import domain.Servant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;

@Repository
public interface ServantRepository extends JpaRepository<Servant, Integer> {

    @Query("select s from Servant s where s.draft=false and s.cancelled=false")
    Collection<Servant> findRequestableServants();
    @Query("select s from Servant s where s.draft=true")
    Collection<Servant> findDraftServants();
    @Query("select s from Servant s where s.cancelled=true")
    Collection<Servant> findCancelledServants(int id);


    @Query("select distinct(l.incidence) from Labor l where l.bill=null and l.incidence.endingDate<?1 and l.incidence.cancelled=false")
    Collection<Incidence> findFacturables(Date limit);
}
