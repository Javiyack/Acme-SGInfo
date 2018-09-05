
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Actor;
import domain.Folder;
import domain.Message;
import domain.PostBox;
import repositories.PostBoxRepository;

@Service
@Transactional
public class PostBoxService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private PostBoxRepository postBoxRepository;

	// Supporting services
	@Autowired
	private ActorService actorService;

	@Autowired
	private MessageService messageService;
	@Autowired
	private FolderService folderService;

	// CRUD Methods

	// Create
	public PostBox create() {
		return new PostBox();
	}

	// Save
	public PostBox save(final PostBox postBox) {
		return postBoxRepository.save(postBox);
	}

	public void checkPrincipal(final PostBox folder) {
	}

	public void createSystemFolders(final Actor actor) {

	}

	public void flush() {
		this.postBoxRepository.flush();

	}

	public Collection<Message> findMessagesByFolderId(Integer folderId) {
		return postBoxRepository.findMessagesByFolderId(folderId);
	}

	public Folder findFolderByMessage(Message message) {
		Actor actor;
		actor = this.actorService.findByPrincipal();
		if (!message.getBroadcast()) {
			Assert.isTrue(message.getSender().equals(actor) || message.getRecipient().equals(actor),
					"msg.not.owned.block");
		}
		return postBoxRepository.findFolderByActorAndMessageId(actor.getId(), message.getId());
	}

	public PostBox findOneByMessage(Message message) {
		Actor actor;
		actor = this.actorService.findByPrincipal();
		if(!message.getBroadcast()) {
			Assert.isTrue(message.getSender().equals(actor) || message.getRecipient().equals(actor), "msg.not.owned.block");
		}
		return postBoxRepository.findOneByMessage(actor.getId(), message.getId());
	}

	public PostBox saveNotification(PostBox pb) {
		return postBoxRepository.save(pb);
	}

	public Collection<Message> findRecivedMessagesByActorId(int actorId) {
		Actor actor;
		actor = this.actorService.findByPrincipal();
		Assert.isTrue(actorId == actor.getId(), "msg.not.owned.block");
		return postBoxRepository.findRecivedMessagesByActorId(actorId);
	}

	public Collection<PostBox> findAllByActor() {
		Actor actor;
		actor = this.actorService.findByPrincipal();
		Assert.notNull(actor, "msg.not.loged.block");
		return postBoxRepository.findAllByActor(actor.getId());
	}

	public Collection<PostBox> findByFolderId(Integer folderId) {
		return postBoxRepository.findByFolderId(folderId);
	}
}
