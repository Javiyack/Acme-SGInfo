
package converters;

import domain.Incidence;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class IncidenceToStringConverter implements Converter<Incidence, String> {

	@Override
	public String convert(final Incidence Servant) {
		String result;
		if (Servant == null)
			result = null;
		else
			result = String.valueOf(Servant.getId());
		return result;
	}
}
