
package converters;

import domain.Labor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

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
