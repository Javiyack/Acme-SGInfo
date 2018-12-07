package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Bill;

@Repository
public interface BillingRepository extends JpaRepository<Bill, Integer> {

	
	@Query("select l.bill from Labor l where l.incidence.id=?1")
	Collection<Bill> findByIncidenceId(int incidenceId);

	@Query("select distinct(l.bill), l.incidence.user.customer from Labor l where l.incidence.user.customer.id=?1")
	Collection<Object> findByCustomerId(int customerId);
	
	@Query("select distinct(l.bill), l.incidence.user.customer from Labor l where l.incidence.user.customer.id=?1"
			+ " and l.incidence.cancelled=false")
	Collection<Object> findPropperByCustomerId(int customerId);

	@Query("select distinct(l.bill), l.incidence.user.customer from Labor l where l.incidence.cancelled=false")
	Collection<Object> findAllPropper();

}
