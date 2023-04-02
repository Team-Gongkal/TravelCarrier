package tc.travelCarrier.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Daily {

    public Daily(){}
    public Daily(Weekly weekly, String dailyDate){
        this.weekly = weekly;
        this.dailyDate = dailyDate;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="DAILY_ID")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="WEEKLY_ID")
    private Weekly weekly;

    @JoinColumn(name="DAILY_DATE")
    private String dailyDate;

    @OneToMany(mappedBy = "daily", cascade = CascadeType.ALL)
    private List<Kword> kwords = new ArrayList<>();




}
