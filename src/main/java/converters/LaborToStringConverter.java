
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Labor;

@Component
@Transactional
public class LaborToStringConverter implements Converter<Labor, String> {

	@Override
	public String convert(final Labor Servant) {
		String result;
		if (Servant == null)
			result = null;
		else
			result = String.valueOf(Servant.getId());
		return result;
	}
}
