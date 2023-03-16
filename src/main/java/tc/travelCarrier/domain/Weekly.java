package tc.travelCarrier.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.With;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter
public class Weekly {

    @Id @Column(name="WEEKLY_ID")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID")
    private User user;

    @Column(name="WEEKLY_TITLE")
    private String title;

    @Column(name="WEEKLY_NATION")
    private String nation;

    @Embedded
    private TravelDate travelDate; //여행기간

    @Embedded
    private CrudDate crudDate; //글 생성일

    @Enumerated(EnumType.STRING)
    @Column(name="WEEKLY_OPEN")
    private OpenStatus status; //공개여부[ME/FOLLOW/ALL]

    @Column(name="WEEKLY_TEXT")
    private String text;

    @OneToMany(mappedBy = "weekly", cascade = CascadeType.ALL)
    private List<Daily> dailys = new ArrayList<>();

    @OneToMany(mappedBy = "weekly", cascade = CascadeType.ALL)
    private List<Gowith> gowiths = new ArrayList<>();

    @OneToOne(mappedBy = "weekly", fetch = FetchType.LAZY)
    private AttachWeekly attachWeekly;

}
