
package services;

import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Actor;
import domain.Administrator;
import domain.Folder;
import domain.Message;
import domain.PostBox;
import repositories.MessageRepository;

@Service
@Transactional
public class MessageService {

	// Validator
	@Autowired
	private Validator validator;

	// Managed repositories ------------------------------------------------
	@Autowired
	private MessageRepository messageRepository;

	// Supporting services
	@Autowired
	private ActorService actorService;
	@Autowired
	private AdministratorService administratorService;
	@Autowired
	private FolderService folderService;
	@Autowired
	private PostBoxService postBoxService;

	// CRUD Methods

	// Create

	public Message create() {
		Message res;
		res = new Message();
		final Actor actor = this.actorService.findByPrincipal();
		res.setSender(actor);
		res.setMoment(new Date(System.currentTimeMillis() - 1));
		return res;
	}

	// Save

	public Message save(final Message message) {
		
		// Compruebo que no sea nulo el mensaje que me pasan
		Assert.notNull(message);
		message.setMoment(new Date(System.currentTimeMillis() - 1));
		// guardo el mensaje en la base de datos
		Message saved = this.messageRepository.save(message);
		return saved;
	
	}

	// Save to move
	public void saveToMove(final Message message, final Folder folder) {

		Assert.notNull(message);
		Assert.notNull(folder);

		final Folder currentFolder = postBoxService.findFolderByMessage(message);

		this.folderService.simpleSave(currentFolder);

		// this.messageRepository.delete(message.getId());

		// Message savedCopy = this.messageRepository.save(copy);

		// Message saved = this.messageRepository.save(message);

		this.folderService.simpleSave(folder);

		// this.messageRepository.save(message);

	}

	// Delete
	public void delete(final Message message) {
		// Compruebo que el mensaje que me pasan no sea nulo
		Assert.notNull(message);

		// Saco el actor que está logueado
		final Actor actor = this.actorService.findByPrincipal();

		// Compruebo que el mensaje que me pasan sea del actor que está logueado
		this.checkPrincipal(message, actor);

		// cojo el trashbox del actor logueado

		// Compruebo que el trashbox del actor logueado no sea nulo

		// si el mensaje que me pasan está en el trashbox del actor logueado:

		// saco la collection de mensajes del trashbox del actor logueado

		// borro el mensaje que me pasan de la collection de mensajes del
		// trashbox

		// actualizo la collection de mensajes del trashbox

		// borro el mensaje del sistema
		this.messageRepository.delete(message);

		// si el mensaje que se quiere borrar no está en el trashbox:

		// Borro el mensaje del folder en el que estaba

		// Meto en el trashbox el mensaje

	}

	// Delete collection
	public void delete(final Iterable<Message> messages) {
		Assert.notNull(messages);
		this.messageRepository.delete(messages);
	}

	// FindOne
	public Message findOne(final int messageId) {
		final Message message = this.messageRepository.findOne(messageId);
		Assert.notNull(message);
		Actor principal;
		principal = this.actorService.findByPrincipal();
		this.checkPrincipal(message, principal);
		return message;

	}

	// Other methods

	public void checkPrincipal(final Message message, final Actor actor) {
		final Collection<Message> messages = this.messageRepository.findAllByActor(actor.getId());
		if(!message.getBroadcast()) {
			Assert.isTrue(messages.contains(message),"msg.not.owned.block");
		}
	}

	public Message reconstruct(final Message m, final BindingResult binding) {

		final Administrator sender = (Administrator) m.getSender();
		final Administrator recipient = sender;
		m.setRecipient(recipient);
		this.validator.validate(m, binding);

		return m;
	}

	public void flush() {
		this.messageRepository.flush();

	}

	public Collection<Message> findAllBySender(int id) {
		return this.messageRepository.findAllBySender(id);
	}

	public void broadcastMessage(Message message) {
		Collection<Folder> folders = new TreeSet<Folder>();
		if(message.isSpam())
			folders = folderService.findAllSpamFolders();
		else
			folders = folderService.findAllNotificationFolders();
			
		for (Folder folder : folders) {
			PostBox pb = postBoxService.create();
			pb.setMessage(message);
			pb.setFolder(folder);
			postBoxService.saveNotification(pb);
		}
	}

	public void saveOnSender(Message message) {
		Actor principal;
		principal = this.actorService.findByPrincipal();
		Folder folder;
		if(message.isSpam())
			folder = folderService.findSpamBoxByActor(principal);
		else
			folder = folderService.findOutBoxByActor(principal);
		PostBox postBox = postBoxService.create();
		postBox.setFolder(folder);
		postBox.setMessage(message);
		postBoxService.save(postBox);
	}

	public void saveOnRecipient(Message message) {
		Folder folder;
		if(message.isSpam())
			folder = folderService.findSpamBoxByActor(message.getRecipient());
		else
			folder = folderService.findInBoxByActor(message.getRecipient());
		PostBox postBox = postBoxService.create();
		postBox.setFolder(folder);
		postBox.setMessage(message);
		postBoxService.save(postBox);

	}

}
