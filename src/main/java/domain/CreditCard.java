package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class CreditCard extends DomainEntity {

	public String holderName;

	public String brandName;

	public String number;

	public String expirationMonth;

	public String expirationYear;

	public String CVV;

	private Customer customer;

}
