
package converters;

import domain.Configuration;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import repositories.ConfigurationRepository;

@Component
@Transactional
public class StringToConfigurationConverter implements Converter<String, Configuration> {

	@Autowired
	private ConfigurationRepository repository;


	@Override
	public Configuration convert(final String str) {
		Configuration result;
		Integer id;
		try {
			if (StringUtils.isEmpty(str))
				result = null;
			else {
				id = Integer.valueOf(str);
				result = this.repository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
