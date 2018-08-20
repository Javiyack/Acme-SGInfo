package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class Folder extends DomainEntity {

	public String name;

	public Boolean system;

	private Folder parent;

	private Actor actor;

}
