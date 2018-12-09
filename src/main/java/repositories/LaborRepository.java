package repositories;

import domain.Labor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;

@Repository
public interface LaborRepository extends JpaRepository<Labor, Integer> {

	@Query("select l from Labor l where l.incidence.technician.id= ?1")
	Collection<Labor> findByTechnicianId(int techId);

	@Query("select l from Labor l where l.incidence.user.id= ?1")
	Collection<Labor> findByUserId(int userId);

	@Query("select l from Labor l where l.incidence.id= ?1")
	Collection<Labor> findByIncidenceId(int incidenceId);
	
	@Query("select l from Labor l where l.incidence.id= ?1")
	Collection<Labor> findByCustomerId(int customerId);
	
	@Query("select l from Labor l where l.incidence.user.customer.id= ?1 and l.bill=null and l.incidence.endingDate<?2")
	Collection<Labor> findFacturablesByCustomerId(int customerId, Date limit);

	@Query("select l from Labor l where l.bill.id= ?1")
	Collection<Labor> findByBillId(int billId);
	
	

}
