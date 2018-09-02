package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Bill;
import domain.Folder;

@Repository
public interface BillingRepository extends JpaRepository<Bill, Integer> {

	
	@Query("select l.bill from Labor l where l.incidence.id=?1")
	Collection<Bill> findByIncidenceId(int incidenceId);

	@Query("select l.bill from Labor l where l.incidence.user.customer.id=?1")
	Collection<Bill> findByCustomerId(int customerId);

	@Query("select distinct(l.bill), l.incidence.user.customer from Labor l")
	Collection<Object> findAllPropper();

}
