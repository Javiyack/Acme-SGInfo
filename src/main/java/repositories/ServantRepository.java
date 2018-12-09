/**
 * 
 */

package repositories;

import domain.Servant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ServantRepository extends JpaRepository<Servant, Integer> {

    @Query("select s from Servant s where s.draft=false and s.cancelled=false")
    Collection<Servant> findRequestableServants();
    @Query("select s from Servant s where s.draft=true")
    Collection<Servant> findDraftServants();
    @Query("select s from Servant s where s.cancelled=true")
    Collection<Servant> findCancelledServants(int id);

}
