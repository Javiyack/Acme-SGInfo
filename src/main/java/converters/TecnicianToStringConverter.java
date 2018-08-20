package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Manager;
import domain.Technician;

@Component
@Transactional
public class TecnicianToStringConverter implements Converter<Technician, String> {

	@Override
	public String convert(final Technician tecnician) {
		String result;
		if (tecnician == null)
			result = null;
		else
			result = String.valueOf(tecnician.getId());
		return result;
	}
}