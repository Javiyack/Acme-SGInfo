package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Technician;

@Repository
public interface TechnicianRepository extends JpaRepository<Technician, Integer> {

	@Query("select t, count(i) as c from Incidence i right join i.technician t group by t order by c asc")
	Collection<Object> findeLessOcuped();

}
