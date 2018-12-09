
package services;

import domain.Actor;
import domain.Constant;
import domain.File;
import forms.FileForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.FileRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;

@Service
@Transactional
public class FileService {

	// Managed repositories ------------------------------------------------
	@Autowired
	private FileRepository fileRepository;

	// Services
	@Autowired
	private ActorService actorService;
	@Autowired
	private Validator validator;

	// Constructor ----------------------------------------------------------
	public FileService() {
		super();
	}

	
	// Custom Methods -------------------------------------------------------

	public boolean canEditFile(final File file) {

		Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.notNull(file);

		return false;
	}

	public boolean canViewFile(final File file) {

		Assert.notNull(file);		
		return false;
	}

	public File save(final File file) {

		if (this.canEditFile(file)) {
			final File saved = this.fileRepository.save(file);
			return saved;
		}

		return null;

	}

	public void delete(final File file) {
		if (this.canEditFile(file))
			this.fileRepository.delete(file);
	}

	public void deleteInBatch(final Collection<File> files) {
		Assert.notNull(files);
		this.fileRepository.deleteInBatch(files);

	}

	public void flush() {
		this.fileRepository.flush();

	}

	public File reconstruct(FileForm fileForm, BindingResult binding) {
		File resultFile;
		if (fileForm.getId() == 0) {
			resultFile = new File();
			switch (fileForm.getType()) {
			case Constant.FILE_INCIDENCE:
				break;

			case Constant.FILE_MESSAGE:
				break;
			
			default:
				break;
			}
			try {
				if (fileForm.getFile().isEmpty()) {
					resultFile.setName(fileForm.getName());
				} else {
					resultFile.setData(fileForm.getFile().getBytes());
					resultFile.setMimeType(fileForm.getFile().getContentType());
					Assert.isTrue(fileForm.getFile().getSize()<52428801, "file.data.size.exceeded.fail");
					resultFile.setSize(fileForm.getFile().getSize());
					resultFile.setUploadDate(new Date(new Date().getTime()-1001));
					if (fileForm.getName().isEmpty()) {
						resultFile.setName(fileForm.getFile().getOriginalFilename());
					} else {
						String originalName = fileForm.getFile().getOriginalFilename();
						String originalExtension = originalName.substring((originalName.lastIndexOf(".")==-1)?originalName.length():(originalName.lastIndexOf(".")));
						String name = fileForm.getName();
						String extension = name.substring((name.lastIndexOf(".")==-1)?name.length():(name.lastIndexOf(".")));
						extension = (originalExtension.equals(extension)) ? "" : originalExtension;
						resultFile.setName(name + extension);
					}
				}

			} catch (Throwable e) {
				this.validator.validate(resultFile, binding);
				Assert.isTrue(fileForm.getFile().getSize()<52428801, "file.data.size.exceeded.fail");
				Assert.notNull(fileForm.getFile(), "file.data.load.fail");
			}		

		} else {
			resultFile = this.findOne(fileForm.getId());
			try {
				if (fileForm.getFile().isEmpty()) {
					if (!fileForm.getName().isEmpty()) {
						String originalName = resultFile.getName();
						String originalExtension = originalName.substring((originalName.lastIndexOf(".")==-1)?originalName.length():(originalName.lastIndexOf(".")));
						String name = fileForm.getName();
						String extension = name.substring((name.lastIndexOf(".")==-1)?name.length():(name.lastIndexOf(".")));
						extension = (originalExtension.equals(extension)) ? "" : originalExtension;
						resultFile.setName(name + extension);
					}else {
						//resultFile.setName(fileForm.getName());
					}
				} else {
					resultFile.setData(fileForm.getFile().getBytes());
					resultFile.setMimeType(fileForm.getFile().getContentType());
					Assert.isTrue(fileForm.getFile().getSize()<52428801, "file.data.size.exceeded.fail");
					resultFile.setSize(fileForm.getFile().getSize());
					resultFile.setUploadDate(new Date(new Date().getTime()-1001));
					if (fileForm.getName().isEmpty()) {
						resultFile.setName(fileForm.getFile().getOriginalFilename());
					} else {
						String originalName = fileForm.getFile().getOriginalFilename();
						String originalExtension = originalName.substring(originalName.lastIndexOf("."));
						String name = fileForm.getName();
						String extension = name.substring(name.lastIndexOf("."));
						extension = (originalExtension.equals(extension)) ? "" : originalExtension;
						resultFile.setName(name + extension);
					}
				}

			} catch (Throwable e) {
				this.validator.validate(resultFile, binding);
				Assert.notNull(fileForm, "file.not.found.fail");
				Assert.notNull(fileForm.getFile(), "file.data.load.fail");
				Assert.isTrue(fileForm.getFile().getSize()<52428801, "file.data.size.exceeded.fail");
				Assert.isTrue(false, "file.data.load.fail");

			}			
		}
		this.validator.validate(resultFile, binding);
		return resultFile;
	}

	// Methods CRUD ---------------------------------------------------------

	public File findOne(final int fileId) {
		File result;

		result = this.fileRepository.findOne(fileId);
		Assert.notNull(result);

		return result;
	}


	public Collection<File> findByForeingnKey(int id) {
		return fileRepository.findByForeingnKey(id);
	}

}
