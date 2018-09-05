
package services;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Actor;
import domain.Folder;
import repositories.FolderRepository;

@Service
@Transactional
public class FolderService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private FolderRepository folderRepository;

	// Supporting services
	@Autowired
	private ActorService actorService;

	@Autowired
	private MessageService messageService;
	@Autowired
	private ConfigurationService configurationService;
	
	// CRUD Methods

	// Create
	public Folder create() {
		Actor actor;
		actor = this.actorService.findByPrincipal();
		final Folder folder = new Folder();
		folder.setSystemFolder(false);
		folder.setActor(actor);
		return folder;
	}

	// Save
	public Folder save(final Folder folder) {
		Assert.notNull(folder);
		Folder savedFolder = null;
		if (folder.getId() != 0) {
			savedFolder = this.folderRepository.findOne(folder.getId());
			this.checkPrincipal(savedFolder);
			this.checkParentIsNoSon(folder);
			savedFolder.setName(folder.getName());
			savedFolder.setParent(folder.getParent());
			savedFolder = this.folderRepository.save(savedFolder);

		} else {
			Actor actor;
			actor = this.actorService.findByPrincipal();
			folder.setActor(actor);
			savedFolder = this.folderRepository.save(folder);
		}
		return savedFolder;
	}

	private void checkParentIsNoSon(Folder folder) {
		// encontramos todos los hijos para evitar ciclos
		Collection<Folder> childFolders = this.findAllSubfolders(folder.getId());
		// comprobamos que el parent no esta en la lista de descendientes para evitar
		// ciclos
		Assert.isTrue(!childFolders.contains(folder), "msg.folder.cicle.prevent.block");
	}

	// Simple Save
	public Folder simpleSave(final Folder f) {
		Assert.notNull(f);

		final Folder folderSaved = this.folderRepository.save(f);

		return folderSaved;

	}

	// Save to move
	public void saveToMove(final Folder folder, final Folder targetFolder) {

		Assert.notNull(targetFolder);
		Assert.notNull(folder);
		folder.setParent(targetFolder);
		// this.folderRepository.save(folder);

	}

	// FindOne
	public Folder findOne(final int folderId) {
		Assert.isTrue(folderId != 0);
		Folder folder;
		folder = this.folderRepository.findOne(folderId);
		Assert.notNull(folder);
		this.checkPrincipal(folder);
		return folder;
	}

	// FindOne to edit
	public Folder findOneToEdit(final int folderId) {
		Assert.isTrue(folderId != 0);
		Folder folder;
		folder = this.folderRepository.findOne(folderId);
		Assert.notNull(folder);
		Assert.isTrue(!folder.getSystemFolder(), "msg.system.folder.block");
		this.checkPrincipal(folder);
		return folder;
	}

	// Delete
	public void delete(final Folder folder) {
		Assert.notNull(folder);
		Assert.isTrue(!folder.getSystemFolder());
		this.checkPrincipal(folder);

		final Collection<Folder> childFolders = this.folderRepository.findChildFolders(folder.getId());

		if (childFolders.size() > 0)
			for (final Folder child : childFolders)
				this.delete(child);

		this.folderRepository.delete(folder);

	}

	public Collection<Folder> findChildFolders(final int folderId) {
		return this.folderRepository.findChildFolders(folderId);
	}

	public void checkPrincipal(final Folder folder) {
		Actor actor;
		actor = this.actorService.findByPrincipal();
		Assert.isTrue(folder.getActor().equals(actor), "msg.not.owned.block");
	}

	public void createSystemFolders(final Actor actor) {
		List<String> names = configurationService.findFolderNames();
		for (String folderName : names) {
			Folder systemFolder = new Folder();
			systemFolder.setName(folderName);
			systemFolder.setActor(actor);
			systemFolder.setSystemFolder(true);
			this.folderRepository.save(systemFolder);
		}
		this.flush();
	}

	public void flush() {
		this.folderRepository.flush();

	}

	public Collection<Folder> findByActor(Actor principal) {
		return folderRepository.findByActor(principal);
	}

	public Collection<Folder> findByActor() {
		Actor actor;
		actor = this.actorService.findByPrincipal();
		return folderRepository.findByActor(actor);
	}

	public Collection<Folder> findFirstlevelFolders(int actorId) {
		return folderRepository.findFirstlevelFolders(actorId);
	}

	public Collection<Folder> findAllNotificationFolders() {
		return folderRepository.findAllNotificationFolders();
	}

	public Folder findOutBoxByActor(Actor actor) {
		Folder result = folderRepository.findOutBoxByActor(actor);
		return result;
	}

	public Folder findInBoxByActor(Actor actor) {
		Folder result = folderRepository.findInBoxByActor(actor);
		return result;
	}

	public Collection<Folder> findAllSpamFolders() {
		return folderRepository.findAllSpamFolders();
	}

	public Folder findSpamBoxByActor(Actor actor) {
		Folder result = folderRepository.findSpamBoxByActor(actor);
		return result;
	}

	public Folder findTrashBoxByActor() {
		Actor actor;
		actor = this.actorService.findByPrincipal();
		Folder result = folderRepository.findTrashBoxByActor(actor);
		return result;
	}

	public Collection<Folder> findAllSubfolders(Integer folderId) {
		Collection<Folder> chidFolders = folderRepository.findChildFolders(folderId);
		Collection<Folder> result = folderRepository.findChildFolders(folderId);
		for (Folder folder : chidFolders) {
			result.addAll(this.findAllSubfolders(folder.getId()));
		}
		return result;
	}

}
