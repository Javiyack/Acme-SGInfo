
package converters;

import domain.Customer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class CustomerToStringConverter implements Converter<Customer, String> {

	@Override
	public String convert(final Customer Customer) {
		String result;
		if (Customer == null)
			result = null;
		else
			result = String.valueOf(Customer.getId());
		return result;
	}
}
