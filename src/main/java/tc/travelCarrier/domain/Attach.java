package tc.travelCarrier.domain;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
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

}
