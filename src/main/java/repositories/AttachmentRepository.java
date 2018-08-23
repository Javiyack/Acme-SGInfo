package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Attachment;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
	@Query("select a from Attachment a where a.message.id = ?1 or a.incidence.id = ?1")
	Collection<Attachment> findByForeingnKey(int Id);
	
	@Query("select a from Attachment a where a.incidence.id = ?1")
	Collection<Attachment> findByIncidenceKey(int Id);
}
