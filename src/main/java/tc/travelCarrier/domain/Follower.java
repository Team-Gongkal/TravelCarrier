package tc.travelCarrier.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "FOLLOWER")
@Getter @Setter
public class Follower {

    Follower(){}

    @Builder
    Follower(User user, User follower, Date fDate){
        this.user = user;
        this.follower = follower;
        this.fDate = fDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="NO")
    private int no;

    @Column(name="FOLLOWER_DATE")
    private Date fDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FOLLOW_ID")
    private User follower;
}
