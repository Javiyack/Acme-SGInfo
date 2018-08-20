
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.ServantRepository;
import domain.Servant;

@Component
@Transactional
public class StringToEquipmentConverter implements Converter<String, Servant> {

	@Autowired
	private ServantRepository	servicioRepository;


	@Override
	public Servant convert(final String str) {
		Servant result;
		Integer id;
		try {
			if (StringUtils.isEmpty(str))
				result = null;
			else {
				id = Integer.valueOf(str);
				result = this.servicioRepository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
