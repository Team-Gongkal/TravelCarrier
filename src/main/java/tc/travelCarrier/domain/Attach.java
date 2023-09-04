package tc.travelCarrier.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Getter @Setter
public abstract class Attach {

    @Id @Column(name="ATTACH_NO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @Column(name="ATTACH_TITLE")
    protected String attachTitle;

    @Column(name="ATTACH_ORIGIN")
    protected String originPath;

    @Column(name="ATTACH_THUMB")
    protected String thumbPath;

    public String getThumbPath() {
        if(thumbPath.indexOf("/static")!=0) return thumbPath.substring(thumbPath.indexOf("/static") + 7);
        else if(thumbPath.indexOf("https:")!=0) return thumbPath.substring(thumbPath.indexOf("https:")+1);
        return thumbPath;
    }

    public String getFullThumbPath() {
        return thumbPath;
    }
}
