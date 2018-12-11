
package controllers;

import domain.Actor;
import domain.Folder;
import domain.Message;
import domain.PostBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.FolderService;
import services.MessageService;
import services.PostBoxService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;

@Controller
@RequestMapping("/folder")
public class FolderController extends AbstractController {

	// Supporting services -----------------------------------------------------
	@Autowired
	private FolderService folderService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private PostBoxService postBoxService;
	@Autowired
	private ActorService actorService;

	// List first level folders
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final Integer folderId) {

		ModelAndView result;
		Folder folder;
		Collection<Folder> folders;
		Collection<Message> messages = new ArrayList<Message>();
		Collection<PostBox> postBoxes;
		result = new ModelAndView("folder/list");
		if (folderId != null) {
			try {
				final Actor actor = this.actorService.findByPrincipal();
				Assert.notNull(actor, "msg.not.logged.block");
				folder = folderService.findOne(folderId);
				Assert.notNull(folder, "msg.not.found.error");
				Assert.isTrue(actor.equals(folder.getActor()), "msg.not.owned.block");
				folders = this.folderService.findChildFolders(folderId);
				messages = postBoxService.findMessagesByFolderId(folderId);
				postBoxes = postBoxService.findByFolderId(folderId);
			}catch (Throwable oops) {
				if (oops.getMessage().startsWith("msg.")) {
					return createMessageModelAndView(oops.getLocalizedMessage(), "folder/list.do");
				} else {
					return this.createMessageModelAndView("msg.commit.error", "folder/list.do");
				}
			}
		} else {
			try {
				final Actor actor = this.actorService.findByPrincipal();
				postBoxes = postBoxService.findAllByActor();
				folders = this.folderService.findFirstlevelFolders(actor.getId());
				messages = postBoxService.findRecivedMessagesByActorId(actor.getId());
				folder = null;
			} catch (Throwable oops) {
				if (oops.getMessage().startsWith("msg.")) {
					return createMessageModelAndView(oops.getLocalizedMessage(), "folder/list.do");
				} else {
					return this.createMessageModelAndView("msg.commit.error", "folder/list.do");
				}
			}
		}
		result.addObject("folder", folder);
		result.addObject("folders", folders);
		result.addObject("messages", messages);
		result.addObject("postBoxes", postBoxes);

		return result;

	}

	// Create folder

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(final Integer folderId) {
		ModelAndView result;
		Folder folder;
		folder = this.folderService.create();
		if (folderId != null) {
			final Folder parentFolder = this.folderService.findOne(folderId);
			folder.setParent(parentFolder);
		}
		result = this.createEditModelAndView(folder);
		Collection<Folder> otherFolders = folderService.findByActor();
		otherFolders.remove(folder);
		result.addObject("otherFolders", otherFolders);
		return result;
	}

	// Edit folder

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(Integer folderId) {
		ModelAndView result;
		Folder folder = this.folderService.findOne(folderId);
		try {
			folder = this.folderService.findOneToEdit(folderId);
			result = this.createEditModelAndView(folder);

		} catch (final Throwable oops) {
			if (oops.getLocalizedMessage().startsWith("msg."))
				result = this.createMessageModelAndView(oops.getLocalizedMessage(),
						"folder/list.do?folderId=" + ((folder != null) ? folder.getId() : ""));
			else
				result = this.createEditModelAndView(folder, "ms.commit.error");
		}
		Collection<Folder> otherFolders = folderService.findByActor();
		otherFolders.remove(folder);
		Collection<Folder> subFolders = folderService.findAllSubfolders(folderId);
		otherFolders.removeAll(subFolders);
		result.addObject("otherFolders", otherFolders);
		return result;
	}

	// Save folder within any other level
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Folder folder, final BindingResult binding) {

		ModelAndView result;
		if (binding.hasErrors())
			result = this.createEditModelAndView(folder);
		else
			try {
				this.folderService.save(folder);
				if (folder.getParent() != null)
					result = new ModelAndView("redirect:list.do?folderId=" + folder.getParent().getId());
				else
					result = new ModelAndView("redirect:list.do?folderId=");

			} catch (final Throwable oops) {
				if (oops.getLocalizedMessage().startsWith("msg."))
					result = this.createMessageModelAndView(oops.getLocalizedMessage(),
							"folder/list.do?folderId=" + ((folder.getParent() != null) ? folder.getParent().getId() : ""));
				else
					result = this.createEditModelAndView(folder, "ms.commit.error");
			}
		return result;

	}

	// Delete folder
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int folderId) {
		ModelAndView result;
		Folder folder;
		folder = this.folderService.findOne(folderId);
		final Folder parentFolder = folder.getParent();

		if (folder.getParent() == null)
			try {
				this.folderService.delete(folder);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = new ModelAndView("redirect:/folder/list.do?folderId=" + folder.getId());

			}
		else
			try {
				this.folderService.delete(folder);

				result = new ModelAndView("redirect:/folder/list.do?folderId=" + parentFolder.getId());
			} catch (final Throwable oops) {
				result = new ModelAndView("redirect:/folder/list.do?folderId=" + folder.getId());

			}

		return result;

	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final Folder folder) {
		ModelAndView result;

		result = this.createEditModelAndView(folder, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Folder folder, final String message) {

		ModelAndView result;
		result = new ModelAndView("folder/create");
		final Folder parentFolder = folder.getParent();
		result.addObject("folder", folder);
		result.addObject("message", message);
		result.addObject("parentFolder", parentFolder);
		result.addObject("requestUri", "folder/edit.do");
		return result;
	}

}
