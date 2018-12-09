package converters;

import domain.Actor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class ActorToStringConverter implements Converter<Actor, String> {

	@Override
	public String convert(Actor actor) {
		String result;
		if (actor == null) {
			result = null;
		} else {
			result = String.valueOf(actor.getId());
		}
		return result;
	}
}
