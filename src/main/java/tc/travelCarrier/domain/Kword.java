package tc.travelCarrier.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter
public class Kword {

    public Kword() {
    }
    public Kword(Daily d, String kword) {
        this.daily = d;
        this.text = kword;
    }
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="KWORD_NO")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DAILY_ID")
    private Daily daily;

    @Column(name="KWORD_TEXT")
    private String text;


}
