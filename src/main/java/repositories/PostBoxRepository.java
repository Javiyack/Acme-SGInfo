package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Folder;
import domain.Message;
import domain.PostBox;

@Repository
public interface PostBoxRepository extends JpaRepository<PostBox, Integer> {

	
	@Query("select distinct(p.message) from PostBox p where p.folder.id=?1 and p.deleted=false")
	Collection<Message> findMessagesByFolderId(Integer folderId);
	
	@Query("select distinct(p.message) from PostBox p where p.folder.actor.id=?1 and p.deleted=false")
	Collection<Message> findMessagesByActorId(Integer actorId);
	
	@Query("select distinct(p.message) from PostBox p where p.message.sender.id=?1 and p.deleted=false")
	Collection<Message> findSendedMessagesByActorId(Integer actorId);
	
	@Query("select distinct(p.message) from PostBox p "
			+ "where p.folder.actor.id=?1 and p.folder.name!='trashbox' "
			+ "and p.folder.name!='outbox' and p.deleted=false")
	Collection<Message> findRecivedMessagesByActorId(Integer actorId);

	@Query("select p.folder from PostBox p where  p.folder.actor.id=?1 and p.message.id=?2 and p.deleted=false")
	Folder findFolderByActorAndMessageId(int actorId, int messageId);

	@Query("select p from PostBox p where  p.folder.actor.id=?1 and p.message.id=?2 and p.deleted=false")
	PostBox findOneByMessage(int actorId, int messageId2);

	@Query("select p from PostBox p "
			+ "where p.folder.actor.id=?1 and p.folder.name!='trashbox' "
			+ "and p.folder.name!='outbox' and p.deleted=false")
	Collection<PostBox> findAllByActor(int id);

	@Query("select distinct(p) from PostBox p where p.folder.id=?1 and p.deleted=false")
	Collection<PostBox> findByFolderId(Integer folderId);
	
	

}
