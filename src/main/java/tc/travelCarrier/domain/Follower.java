package tc.travelCarrier.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "FOLLOWER")
@Getter @Setter
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="NO")
    private int no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FOLLOWER_ID")
    private User follower;
}
