package repositories;

import domain.Incidence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;

@Repository
public interface IncidenceRepository extends JpaRepository<Incidence, Integer> {
	@Query("select i from Incidence i where i.user.customer.id = ?1")
	Collection<Incidence> findByUserAccountId(int userAccountId);

	@Query("select distinct(l.incidence) from Labor l where l.bill=null and l.incidence.endingDate<?1 and l.incidence.cancelled=false")
	Collection<Incidence> findFacturables(Date limit);
}
