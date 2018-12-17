package repositories;

import domain.Bill;
import domain.MonthlyDue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

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
	Collection<Object> findAllPropperLaborBills();

	@Query("select distinct(due.bill), due.request.responsible.customer from MonthlyDue due")
	Collection<Object> findAllPropperServiceBills();

	@Query("select distinct(due.bill), due.request.responsible.customer from MonthlyDue due where due.request.responsible.customer.id=?1")
	Collection<Object> findAllPropperServiceBillsByCustomerId(int customerId);

	@Query("select distinct(due) from MonthlyDue due where due.bill=?1")
	Collection<MonthlyDue> findDuesByBill(Bill bill);
}
