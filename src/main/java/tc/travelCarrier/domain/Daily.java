package tc.travelCarrier.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tc.travelCarrier.dto.DailyForm;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @ToString
public class Daily {

    public Daily(){}
    public Daily(Weekly weekly, String dailyDate){
        this.weekly = weekly;
        this.dailyDate = dailyDate;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="DAILY_ID")
    private int id;

    @Column(name="DAILY_DATE")
    private String dailyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="WEEKLY_ID")
    private Weekly weekly;

    @OneToMany(mappedBy = "daily", cascade = CascadeType.ALL)
    private List<Kword> kwords = new ArrayList<>();

    @OneToMany(mappedBy = "daily", cascade = CascadeType.ALL)
    private List<AttachDaily> attachDailies = new ArrayList<>();


    //==생성메소드
    public static Daily createDaily(String day, List<DailyForm> formList){
        Daily daily = new Daily();
        daily.setDailyDate(day);
        daily.setAttachDailies(daily.createAttachDailies(formList));
        return daily;
    }

    private List<AttachDaily> createAttachDailies(List<DailyForm> formList) {
        List<AttachDaily> attachDailyList = new ArrayList<>();
        for(DailyForm form : formList) {
            AttachDaily ad = AttachDaily.createAttachDaily(form);
            ad.setDaily(this);
            attachDailyList.add(ad);
        }
        return attachDailyList;
    }

    //weekly에 dailys 추가했을때 daily에도 this로 weekly 셋탕
    //daily에 attach_dailys 추가했을때 attach_daily에도 this로 daily 셋팅
    // 즉 daily에서 attach_daily객체에 daily 셋팅해줘야함.


}
