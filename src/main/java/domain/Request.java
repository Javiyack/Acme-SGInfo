
package domain;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;

@Entity
@Access(AccessType.PROPERTY)
public class Request extends DomainEntity {
    /*
     *  For every request, the system must store the
        moment when the request is made; if the corresponding item is free, then no credit card
        must be included in the request; otherwise a valid credit card that must not expire in less
        than 30 days must be included.
    */
    //Relationships
    private Servant servant;
    private Responsible responsible;
    private Date creationMoment;
    private Date startingDay;
    private Date endingDay;
    private String status;
    private String rejectionReason;
    private String comments;
    public static final String	PENDING			= "PENDING";
    public static final String	ACCEPTED	    = "ACCEPTED";
    public static final String	REJECTED	    = "REJECTED";

    //Constructor
    public Request() {
        super();
    }

    @NotNull
    @Valid
    @ManyToOne(optional = false)
    public Servant getServant() {
        return servant;
    }

    public void setServant(Servant servant) {
        this.servant = servant;
    }

    @NotNull
    @Valid
    @ManyToOne(optional = false)
    public Responsible getResponsible() {
        return responsible;
    }

    public void setResponsible(Responsible responsible) {
        this.responsible = responsible;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    public Date getCreationMoment() {
        return this.creationMoment;
    }

    public void setCreationMoment(final Date moment) {
        this.creationMoment = moment;
    }

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getStartingDay() {
        return startingDay;
    }

    public void setStartingDay(Date startingDay) {
        this.startingDay = startingDay;
    }

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getEndingDay() {
        return endingDay;
    }

    public void setEndingDay(Date endingDay) {
        this.endingDay = endingDay;
    }
    @NotBlank
    @SafeHtml
    @Pattern(regexp = "^(PENDING|ACCEPTED|REJECTED)$")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   @SafeHtml
   public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @SafeHtml
    @Size(max = 255)
    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
