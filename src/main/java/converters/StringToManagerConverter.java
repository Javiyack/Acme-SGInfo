package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Manager;
import repositories.ManagerRepository;

@Component
@Transactional
public class StringToManagerConverter implements Converter<String, Manager> {

	@Autowired
	private ManagerRepository	managerRepository;


	@Override
	public Manager convert(final String str) {
		Manager result;
		Integer id;
		try {
			if (StringUtils.isEmpty(str))
				result = null;
			else {
				id = Integer.valueOf(str);
				result = this.managerRepository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}

