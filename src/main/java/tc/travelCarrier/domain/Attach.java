package tc.travelCarrier.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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
        return thumbPath.substring(81);
    }
}
