package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Valuation;

@Component
@Transactional
public class ValuationToStringConverter implements Converter<Valuation, String> {

	@Override
	public String convert(final Valuation valuation) {
		String result;
		if (valuation == null)
			result = null;
		else
			result = String.valueOf(valuation.getId());
		return result;
	}
}
