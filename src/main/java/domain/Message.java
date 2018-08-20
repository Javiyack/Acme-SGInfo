package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class Message extends DomainEntity {

	private Date date;

	private String subject;

	private String body;

	private String priority;

	private Boolean notification;

	private Boolean spam;

	private Attachment[] atachment;

	private Actor actor;

}
