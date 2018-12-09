package repositories;

import domain.Actor;
import domain.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Integer> {

	
	@Query("select f from Folder f where f.parent.id=?1")
	Collection<Folder> findChildFolders(int folderId);

	@Query("select f from Folder f where f.actor=?1")
	Collection<Folder> findByActor(Actor principal);

	@Query("select f from Folder f where f.actor.id=?1 and f.parent=null")
	Collection<Folder> findFirstlevelFolders(int actorId);

	@Query("select f from Folder f where f.name='notificationbox' and f.systemFolder=true")
	Collection<Folder> findAllNotificationFolders();
	
	@Query("select f from Folder f where f.name='spambox' and f.systemFolder=true")
	Collection<Folder> findAllSpamFolders();
	
	@Query("select f from Folder f where f.name='outbox' and f.systemFolder=true and f.actor=?1")
	Folder findOutBoxByActor(Actor actor);

	@Query("select f from Folder f where f.name='inbox' and f.systemFolder=true and f.actor=?1")
	Folder findInBoxByActor(Actor principal);
	
	@Query("select f from Folder f where f.name='notificationbox' and f.systemFolder=true and f.actor=?1")
	Folder findNotificationBoxByActor(Actor principal);
	
	@Query("select f from Folder f where f.name='spambox' and f.systemFolder=true and f.actor=?1")
	Folder findSpamBoxByActor(Actor principal);
	
	@Query("select f from Folder f where f.name='trashbox' and f.systemFolder=true and f.actor=?1")
	Folder findTrashBoxByActor(Actor principal);


	
	

}
