
package converters;

import domain.TabooWord;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import repositories.TabooWordRepository;

@Component
@Transactional
public class StringToTabooWordConverter implements Converter<String, TabooWord> {

	@Autowired
	private TabooWordRepository	repository;


	@Override
	public TabooWord convert(final String str) {
		TabooWord result;
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
