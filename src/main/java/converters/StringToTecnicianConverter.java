package converters;

import domain.Technician;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import repositories.TechnicianRepository;
@Component
@Transactional
public class StringToTecnicianConverter implements Converter<String, Technician> {

	@Autowired
	private TechnicianRepository	tecnicianRepository; 


	@Override
	public Technician convert(final String str) {
		Technician result;
		Integer id;
		try {
			if (StringUtils.isEmpty(str))
				result = null;
			else {
				id = Integer.valueOf(str);
				result = this.tecnicianRepository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}

