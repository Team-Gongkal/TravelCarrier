package tc.travelCarrier.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tc.travelCarrier.dto.DailyForm;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ATTACH_DAILY")
@DiscriminatorValue("Daily")
@Getter @Setter @ToString
public class AttachDaily extends Attach{


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name="DAILY_ID")
    private Daily daily;

    @Column(name = "ATTACH_DAILY_TITLE")
    private String title;

    @Column(name = "ATTACH_DAILY_TEXT")
    private String text;


    @Column(name = "ATTACH_DAILY_SORT")
    private int sort;

    @Column(name = "ATTACH_THUMB")
    private boolean thumb;

    @OneToMany(mappedBy ="attachDaily", cascade = CascadeType.ALL)
    private List<Reply> replyList;

    //생성메소드
    
    //연관관계메소드
    public static AttachDaily createAttachDaily(DailyForm dailyForm){
        AttachDaily attachDaily = new AttachDaily();
        attachDaily.setAttachTitle(dailyForm.getNewFileName());
        attachDaily.setThumbPath(dailyForm.getPath());
        attachDaily.setTitle(dailyForm.getTitle());
        attachDaily.setText(dailyForm.getText());
        attachDaily.setSort(dailyForm.getSort());
        attachDaily.setThumb(dailyForm.getThumb()!=0); //0이 아니면, 즉 1이면 true 반환
        return attachDaily;
    }

    public void updateField(DailyForm form) {
        title = form.getTitle();
        text = form.getText();
        sort = form.getSort();
        if(form.getThumb() == 1) thumb = true;
        else if(form.getThumb() ==0) thumb = false;
    }

    public void updateFile(String newTitle, String thumbPath) {
        attachTitle = newTitle;
        this.thumbPath = thumbPath;
    }
}
