package tc.travelCarrier.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "ATTACH_DAILY")
@DiscriminatorValue("Daily")
@Getter @Setter
public class AttachDaily extends Attach{

    public AttachDaily(){}
    @Builder
    public AttachDaily(int id, String attachTitle, String thumb, Daily daily,
                       String title, String text, int sort, boolean isThumb){
        this.id = id;
        this.attachTitle = attachTitle;
        this.thumbPath = thumb;
        this.daily = daily;
        this.title = title;
        this.text = text;
        this.sort = sort;
        this.isThumb = isThumb;
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
