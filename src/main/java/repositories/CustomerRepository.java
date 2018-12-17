package repositories;

import domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	@Query("select distinct(l.incidence.user.customer) from Labor l where l.bill.id= ?1")
	Customer findByBillId(int id);

	@Query("select a.customer.id from Actor a where a.id=?1")
	Integer findIdByPrincipalId(int id);

	@Query("select c from Customer c where c.active=true")
	Collection<Customer> findAllActive();

	@Query("select c from Customer c where c.biller = true")
	Customer findBiller();
}
