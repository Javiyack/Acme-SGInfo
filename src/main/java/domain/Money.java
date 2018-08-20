package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.PROPERTY)
public class Money extends DomainEntity {

	public Double amount;

	public String currency;

	public Money() {
		super();
	}

	public Money add(Money money) {
		return null;
	}

	public Money subtract(Money money) {
		return null;
	}

}
