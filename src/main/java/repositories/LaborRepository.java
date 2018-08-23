package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Labor;

@Repository
public interface LaborRepository extends JpaRepository<Labor, Integer> {

	@Query("select l from Labor l where l.incidence.technician.id= ?1")
	Collection<Labor> findByTechnicianId(int techId);

	@Query("select l from Labor l where l.incidence.user.id= ?1")
	Collection<Labor> findByUserId(int userId);

	@Query("select l from Labor l where l.incidence.id= ?1")
	Collection<Labor> findByIncidenceId(int incidenceId);

}
