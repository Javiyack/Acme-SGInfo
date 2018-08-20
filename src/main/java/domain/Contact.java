package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class Contact extends DomainEntity {

	public String name;

	public String cargo;

	public String email;

	public String[] phone;

	private Customer customer;

	private Location location;

}
