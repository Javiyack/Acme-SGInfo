/**
 * 
 */

package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Servant;

@Repository
public interface ServantRepository extends JpaRepository<Servant, Integer> {

}
