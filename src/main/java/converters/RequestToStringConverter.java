
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Servant;

@Component
@Transactional
public class RequestToStringConverter implements Converter<Servant, String> {

	@Override
	public String convert(final Servant Servant) {
		String result;
		if (Servant == null)
			result = null;
		else
			result = String.valueOf(Servant.getId());
		return result;
	}
}
