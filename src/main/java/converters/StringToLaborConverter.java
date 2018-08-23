
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Labor;
import repositories.LaborRepository;

@Component
@Transactional
public class StringToLaborConverter implements Converter<String, Labor> {

	@Autowired
	private LaborRepository	laborRepository;


	@Override
	public Labor convert(final String str) {
		Labor result;
		Integer id;
		try {
			if (StringUtils.isEmpty(str))
				result = null;
			else {
				id = Integer.valueOf(str);
				result = this.laborRepository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
