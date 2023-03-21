package tc.travelCarrier.domain;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public abstract class Attach {

    @Id @Column(name="ATTACH_NO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="ATTACH_TITLE")
    private String attachTitle;

    @Column(name="ATTACH_ORIGIN")
    private String origin;

    @Column(name="ATTACH_THUMB")
    private String thumb;

}
