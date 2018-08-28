package controllers;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.File;
import forms.FileForm;
import services.ActorService;
import services.FileService;

@Controller
@RequestMapping("/file")
public class FileController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private FileService fileService;
	
	//List
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam int id, Integer pageSize) {
		
		ModelAndView result;
		pageSize = pageSize!=null?pageSize:5;							
		Collection<File> files = this.fileService.findByForeingnKey(id);
		result = new ModelAndView("file/list");
		result.addObject("files", files);
		result.addObject("pageSize", pageSize);
		result.addObject("pageSize", pageSize);
		result.addObject("requestUri", "file/list.do");
		return result;
	}
	
	// Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int fileId) {
		final ModelAndView result;

		File file = this.fileService.findOne(fileId);
		Assert.isTrue(this.fileService.canViewFile(file));

		FileForm fileForm = new FileForm();
		fileForm.setFk(getFk(file));
		fileForm.setId(fileId);
		fileForm.setType(getType(file));
		result = new ModelAndView("file/display");
		result.addObject("fileForm", fileForm);
		result.addObject("uploadDate", file.getUploadDate());
		result.addObject("name", file.getName());
		return result;
	}

	// Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int id, @RequestParam final String type) {
		ModelAndView result;

		FileForm fileForm = new FileForm();
		fileForm.setFk(id);
		fileForm.setId(0);
		fileForm.setType(type);
		result = this.createEditModelAndView(fileForm);
		return result;
	}

	// Edit
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam int fileId) {
		ModelAndView result;

		File file = this.fileService.findOne(fileId);

		Assert.isTrue(this.fileService.canEditFile(file));
		FileForm fileForm = new FileForm();
		fileForm.setFk(getFk(file));
		fileForm.setId(fileId);
		fileForm.setType(getType(file));
		fileForm.setName(file.getName());
		result = this.createEditModelAndView(fileForm);
		result.addObject("name", file.getName());
		return result;
	}

	
	// Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(FileForm fileForm, BindingResult binding) {
		ModelAndView result = null;

		try {
			File file = this.fileService.reconstruct(fileForm, binding);
			if (binding.hasErrors()) {
				result = this.createEditModelAndView(fileForm);
				for (ObjectError objectError : binding.getAllErrors()) {
					if (objectError.getCode().contains("NotNull")) {
						result.addObject("message", "file.empty.validation.error");
					}
				}

			} else {
				try {
					fileService.save(file);
					result = this.getRedirect(file);
				} catch (Throwable oops) {
					// Fallo en el save
					result = createEditModelAndView(fileForm, "file.commit.error");
				}
			}
		} catch (Throwable oops) {
			// Fallo en la reconstruccion
			if(oops.getLocalizedMessage().contains("file."))
				result = this.createEditModelAndView(fileForm, oops.getLocalizedMessage());
			else
				result = this.createEditModelAndView(fileForm, "file.reconstruct.fail");
		}
		return result;
	}

	// Delete
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(File file, BindingResult binding) {

		ModelAndView result = null;

		Assert.isTrue(this.fileService.canEditFile(file));

		try {
			fileService.delete(file);
			result = this.getRedirect(file);

		} catch (Throwable oops) {
			result = createEditModelAndView(file, "file.commit.error");
		}

		return result;
	}

	// Delete
	@RequestMapping(value = "/delte", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam int fileId) {

		ModelAndView result = null;
		File file = this.fileService.findOne(fileId);
		Assert.isTrue(file != null, "file.not.found.error");
		Assert.isTrue(this.fileService.canEditFile(file));

		try {
			fileService.delete(file);
			result = this.getRedirect(file);

		} catch (Throwable oops) {
			result = createEditModelAndView(file, "file.commit.error");
		}

		return result;
	}

	// Download
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public ModelAndView downloadDocument(@RequestParam int fileId, HttpServletResponse response) throws IOException {
		File file = this.fileService.findOne(fileId);
		Assert.isTrue(file != null, "file.not.found.error");
		Assert.isTrue(this.fileService.canViewFile(file));
		response.setContentType(file.getMimeType());
		response.setContentLength(file.getData().length);
		response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

		FileCopyUtils.copy(file.getData(), response.getOutputStream());

		return this.getRedirect(file);
	}

	// Ancillary Methods

	protected ModelAndView createEditModelAndView(final File file) {
		ModelAndView result;

		result = this.createEditModelAndView(file, null);

		return result;
	}

	private ModelAndView createEditModelAndView(FileForm fileForm) {
		ModelAndView result;

		result = this.createEditModelAndView(fileForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final File file, final String messageCode) {
		ModelAndView result;

		if (file.getId() != 0)
			result = new ModelAndView("file/edit");
		else
			result = new ModelAndView("file/create");

		result.addObject("file", file);
		result.addObject("message", messageCode);

		return result;
	}

	private ModelAndView createEditModelAndView(FileForm fileForm, String messageCode) {
		ModelAndView result;

		if (fileForm.getId() != 0)
			result = new ModelAndView("file/edit");
		else
			result = new ModelAndView("file/create");

		result.addObject("fileForm", fileForm);
		result.addObject("message", messageCode);

		return result;
	}

	private ModelAndView getRedirect(File file) {

		if (file.getIncidence() != null) {
			return new ModelAndView("redirect:/incidence/display.do?id=" + file.getIncidence().getId());
		}
		if (file.getMessage() != null) {
			return new ModelAndView("redirect:/message/display.do?id=" + file.getMessage().getId());
		}
		
		return null;
	}

	private Integer getFk(File file) {
		Integer res = null;
		if (file.getIncidence() != null) {
			res = file.getIncidence().getId();
		}else if (file.getMessage() != null) {
			res = file.getMessage().getId();
		}

		return res;
	}

	private String getType(File file) {
		String res = "";
		if (file.getIncidence() != null) {
			res = "incidence";
		}
		if (file.getMessage() != null) {
			res = "message";
		}
		return res;
	}

}
