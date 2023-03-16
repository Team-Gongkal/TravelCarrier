package tc.travelCarrier.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Kword {

    @Id @GeneratedValue
    @Column(name="KWORD_NO")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "DAILY_ID")
    private Daily daily;

    @Column(name="KWORD_TEXT")
    private String text;
}
