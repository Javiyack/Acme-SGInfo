package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.ValuationRepository;

import domain.Valuation;

@Component
@Transactional
public class StringToValuationConverter implements Converter<String, Valuation>{
	@Autowired
	private ValuationRepository	valuationRepository;


	@Override
	public Valuation convert(final String str) {
		Valuation result;
		Integer id;
		try {
			if (StringUtils.isEmpty(str))
				result = null;
			else {
				id = Integer.valueOf(str);
				result = this.valuationRepository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
