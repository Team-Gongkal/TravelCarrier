package tc.travelCarrier.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Daily {

    @Id
    @Column(name="DAILY_ID")
    private String id;

    @ManyToOne
    @JoinColumn(name="WEEKLY_ID")
    private Weekly weekly;

    @OneToMany(mappedBy = "daily")
    private List<Kword> kwords = new ArrayList<>();
}
