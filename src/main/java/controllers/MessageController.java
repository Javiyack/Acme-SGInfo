package controllers;

import domain.Actor;
import domain.Folder;
import domain.Message;
import domain.PostBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import security.Authority;
import services.*;

import javax.validation.Valid;
import java.util.Collection;

@Controller
@RequestMapping("/message")
public class MessageController extends AbstractController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private FolderService folderService;
    @Autowired
    private PostBoxService postBoxService;
    @Autowired
    private AdministratorService administratorService;

    // Create
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create() {
        ModelAndView result;
        Message message;
        message = messageService.create();
        result = this.createEditModelAndView(message);
        return result;
    }

    // Create to move
    @RequestMapping(value = "/move", method = RequestMethod.GET)
    public ModelAndView createMove(@RequestParam int messageId) {
        ModelAndView result;
        Message m;
        Folder f;

        m = messageService.findOne(messageId);
        f = postBoxService.findFolderByMessage(m);
        Actor principal = actorService.findByPrincipal();
        Collection<Folder> folders = folderService.findByActor(principal);
        result = new ModelAndView("message/move");
        result.addObject("m", m);
        result.addObject("folder", f);
        result.addObject("message", null);
        result.addObject("folders", folders);
        return result;

    }

    // Save
    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
    public ModelAndView send(@ModelAttribute("modelMessage") @Valid Message message, BindingResult binding) {
        ModelAndView result;
        if (binding.hasErrors()) {
            result = this.createEditModelAndView(message);
        } else {
            try {
                message.setSpam(this.administratorService.checkIsSpam(message.getSubject(), message.getBody()));
                message = messageService.save(message);
                if (message.getBroadcast()) {
                    message.setRecipient(message.getSender());
                    messageService.broadcastMessage(message);
                } else {
                    messageService.saveOnSender(message);
                    messageService.saveOnRecipient(message);
                    result = new ModelAndView("redirect:/folder/list.do");
                }
                result = new ModelAndView("redirect:/folder/list.do");
            } catch (Throwable oops) {
                result = createEditModelAndView(message, "ms.commit.error");
            }
        }
        return result;
    }


    // Save to move
    @RequestMapping(value = "/saveMove", method = RequestMethod.GET)
    public ModelAndView saveMove(@RequestParam(required = true) int messageId,
                                 @RequestParam(required = true) int folderId) {
        ModelAndView result;
        Message message = messageService.findOne(messageId);
        Folder choosedFolder = folderService.findOne(folderId);
        PostBox postBox = postBoxService.findOneByMessage(message);
        postBox.setFolder(choosedFolder);
        try {
            postBox = postBoxService.save(postBox);
            result = new ModelAndView("redirect:/folder/list.do?folderId=" + choosedFolder.getId());

        } catch (Throwable oops) {
            Actor principal = actorService.findByPrincipal();
            Collection<Folder> folders = folderService.findByActor(principal);
            result = new ModelAndView("message/move");
            result.addObject("m", message);
            result.addObject("message", "ms.commit.error");
            result.addObject("folders", folders);

        }

        return result;
    }

    // Display
    @RequestMapping(value = "/display", method = RequestMethod.GET)
    public ModelAndView display(@RequestParam int messageId) {
        ModelAndView result = new ModelAndView("/");
        Message m;
        Folder folder;
        try {
            m = messageService.findOne(messageId);
            folder = postBoxService.findFolderByMessage(m);
            result = new ModelAndView("message/display");
            result.addObject("m", m);
            result.addObject("folder", folder);
        } catch (Throwable oops) {
            if (oops.getMessage().startsWith("msg.")) {
                result = this.createMessageModelAndView(oops.getLocalizedMessage(),
                        "folder/list.do");
            } else {
                result = this.createMessageModelAndView("msg.commit.error",
                        "folder/list.do");
            }
        }
        return result;
    }

    // Delete
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam int messageId, Integer folderId) {
        ModelAndView result;
        Message message;
        Folder folder = null;
        if (folderId != null) {
            folder = folderService.findOne(folderId);
        }
        message = messageService.findOne(messageId);
        PostBox postBox = postBoxService.findOneByMessage(message);
        if (folder != null && folder.getName().equals("trashbox")) {
            postBox.setDeleted(true);
        } else {
            Folder trashBox = folderService.findTrashBoxByActor();
            postBox.setFolder(trashBox);
        }
        try {
            this.postBoxService.save(postBox);
            result = new ModelAndView("redirect:/folder/list.do?folderId=" + ((folder != null) ? folder.getId() : ""));
        } catch (final Throwable oops) {
            result = new ModelAndView("redirect:/message/display.do?messageId=" + message.getId());
        }

        return result;

    }

    // Ancillary methods

    protected ModelAndView createEditModelAndView(final Message m) {
        ModelAndView result;

        result = this.createEditModelAndView(m, null);

        return result;
    }

    protected ModelAndView createEditModelAndView(final Message m, final String messageCode) {
        ModelAndView result;
        Actor actor = actorService.findByPrincipal();
        String authority = actorService.getType(actor.getUserAccount());
        Collection<Actor> actors;
        if (authority.equals(Authority.RESPONSIBLE) || authority.equals(Authority.USER)){
            actors = actorService.findWorkers(actor);
            actors.addAll(actorService.findInternalStaff());
        }
        else
            actors = actorService.findAll();
        actors.remove(actor);
        result = new ModelAndView("message/create");
        result.addObject("modelMessage", m);
        result.addObject("message", messageCode);
        result.addObject("actors", actors);
        result.addObject("requestUri", "message/edit.do");

        return result;
    }

}
