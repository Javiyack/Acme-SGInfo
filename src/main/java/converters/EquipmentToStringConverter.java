
package converters;

import domain.Servant;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class EquipmentToStringConverter implements Converter<Servant, String> {

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
