package tc.travelCarrier.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.With;
import org.springframework.web.multipart.MultipartFile;
import tc.travelCarrier.dto.DailyForm;
import tc.travelCarrier.dto.WeeklyForm;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.util.*;

@Entity
@ToString
@Getter @Setter
public class Weekly {

    @Id @Column(name="WEEKLY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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
    private List<Daily> dailys  = new ArrayList<>();

    @OneToMany(mappedBy = "weekly", cascade = CascadeType.ALL)
    private List<Gowith> gowiths  = new ArrayList<>();

    @OneToOne(mappedBy = "weekly", fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
    private AttachWeekly attachWeekly;

    //==비즈니스로직==//
    // 위클리 등록하면 daily, gowiths, attachWeekly에 값 들어가는 동작 만드느건가?
    //재고수량처럼 데이터를 가지고 있는 엔티티쪽에 작성하는거래!


    //연관관계 메소드
    public void addGowith(Gowith gowith) {
        gowiths.add(gowith);
        gowith.setWeekly(this); //추가한걸 연관관계 주인인 파라미터 한테도!!
    }
    public void addDailies(Daily daily) {
        dailys.add(daily);
        daily.setWeekly(this); //이러면 dailys에 추가된 daily들에 weekly가 셋팅됨!!!
    }

    //수정
    public void updateWeekly(WeeklyForm form) throws ParseException {
        this.title = form.getTitle();
        this.text = form.getText();
        this.nation = form.getNation();
        this.travelDate = new TravelDate(form.getSdate(), form.getEdate());
        //this.status = form.getStatus();
    }
    //생성메소드
    public static Weekly createWeekly(User user, AttachWeekly attachWeekly, String title, String nation,
                  TravelDate travelDate, CrudDate crudDate, OpenStatus status,
                  String text, List<User> goWithList){
        Weekly weekly = new Weekly();
        weekly.setUser(user);
        weekly.setAttachWeekly(attachWeekly);
        weekly.setTitle(title);
        weekly.setNation(nation);
        weekly.setTravelDate(travelDate);
        weekly.setCrudDate(crudDate);
        weekly.setStatus(status);
        weekly.setText(text);

        if(goWithList.size() != 0){
            for(User gw : goWithList) {
                weekly.addGowith(new Gowith(gw));
            }
        }

        return weekly;
    }

    public void updateGowith(User user) {
        Gowith gw = new Gowith(user);
        this.gowiths.add(gw);
        gw.setWeekly(this);
    }
}
