
package converters;

import domain.Incidence;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import repositories.IncidenceRepository;

@Component
@Transactional
public class StringToIncidenceConverter implements Converter<String, Incidence> {

	@Autowired
	private IncidenceRepository	incidenceRepository;


	@Override
	public Incidence convert(final String str) {
		Incidence result;
		Integer id;
		try {
			if (StringUtils.isEmpty(str))
				result = null;
			else {
				id = Integer.valueOf(str);
				result = this.incidenceRepository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
