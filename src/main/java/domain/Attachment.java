package domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Access(AccessType.PROPERTY)
public class Attachment extends DomainEntity {

	private String name;

	private Date uploadDate;

	private byte[] data;

	private String[] mineType;

	private long size;

	private Message message;

	private Incidence incidence;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String[] getMineType() {
		return mineType;
	}

	public void setMineType(String[] mineType) {
		this.mineType = mineType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@NotNull
	@ManyToOne(optional = false)
	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	@NotNull
	@ManyToOne(optional = false)
	public Incidence getIncidence() {
		return incidence;
	}

	public void setIncidence(Incidence incidence) {
		this.incidence = incidence;
	}

}
