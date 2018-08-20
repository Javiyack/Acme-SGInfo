package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class Attachment extends DomainEntity {

	public String name;

	public Date uploadDate;

	public byte[] data;

	public String[] mineType;

	public long size;

	private Message message;

	private Incidence incidence;

}
