
package repositories;

import domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	@Query("select a from Actor a where a.customer.id = ?1")
	Collection<User> findAllByCustomerId(int customerId);

	
}
