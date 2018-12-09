
package forms;

import domain.Servant;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

public class ServantForm {


    private String name;
    private String description;
    private Double price;
    private String picture;
    private boolean cancelled;
    private boolean draft;
    private int id;
    private int version;

//Constructors -------------------------

    public ServantForm() {
        super();
        this.id = 0;
        this.version = 0;
    }

    public ServantForm(Servant servant) {
        super();
        this.id = servant.getId();
        this.version = servant.getVersion();
        setName(servant.getName());
        setDescription(servant.getDescription());
        setPrice(servant.getPrice());
        setPicture(servant.getPicture());
        setDraft(servant.isDraft());
        setCancelled(servant.isCancelled());
        this.version = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SafeHtml(whitelistType = WhiteListType.NONE)
    @NotBlank
    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @NotNull
    @Digits(integer=9, fraction=2)
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    public int getId() {
        return this.id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }



    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
