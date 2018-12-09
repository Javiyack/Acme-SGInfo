package repositories;

import domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {
	@Query("select a from Attachment a where a.message.id = ?1 or a.incidence.id = ?1")
	Collection<File> findByForeingnKey(int id);
	
	@Query("select a from Attachment a where a.incidence.id = ?1")
	Collection<File> findByIncidenceKey(int id);
}
