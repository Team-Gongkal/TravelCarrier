package tc.travelCarrier.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Reply {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="REPLY_ID")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ATTACH_NO")
    private AttachDaily attachDaily;

    @Column(name="REPLY_TEXT")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID")
    private User user;

    @Embedded
    private CrudDate crudDate;

    @Column(name = "REPLY_ORIGIN")
    private String origin;


}
