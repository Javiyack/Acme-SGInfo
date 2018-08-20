package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class PostBox extends DomainEntity {

	private Folder folder;

	private Message message;

}
