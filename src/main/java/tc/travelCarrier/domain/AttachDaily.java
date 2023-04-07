package tc.travelCarrier.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import tc.travelCarrier.dto.DailyForm;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "ATTACH_DAILY")
@DiscriminatorValue("Daily")
@Getter @Setter
public class AttachDaily extends Attach{

    public AttachDaily(){}
    public AttachDaily(String[] saveArr, DailyForm form){
        this.attachTitle = saveArr[0];
        this.thumbPath = saveArr[1];;
        this.title = form.getTitle();
        this.text = form.getText();
        this.sort = form.getSort();
        //this.isThumb = form.getThumb();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="DAILY_ID")
    private Daily daily;

    @Column(name = "ATTACH_DAILY_TITLE")
    private String title;

    @Column(name = "ATTACH_DAILY_TEXT")
    private String text;

    @Column(name = "ATTACH_DAILY_SORT")
    private int sort;

    @Column(name = "ATTACH_THUMB")
    private boolean isThumb;
}
