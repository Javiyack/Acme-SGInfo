
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Attachment;
import domain.Incidence;
import repositories.AttachmentRepository;
import repositories.IncidenceRepository;

@Component
@Transactional
public class StringToAttachmentConverter implements Converter<String, Attachment> {

	@Autowired
	private AttachmentRepository	attachmentRepository;


	@Override
	public Attachment convert(final String str) {
		Attachment result;
		Integer id;
		try {
			if (StringUtils.isEmpty(str))
				result = null;
			else {
				id = Integer.valueOf(str);
				result = this.attachmentRepository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
