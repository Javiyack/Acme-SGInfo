package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class Location extends DomainEntity {

	public String address;

	public Coordinate coordinates;

	public int postalCode;

	private Customer customer;

}
