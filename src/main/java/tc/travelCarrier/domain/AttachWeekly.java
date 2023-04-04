package tc.travelCarrier.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ATTACH_WEEKLY")
@DiscriminatorValue("Weekly")
@Getter @Setter
public class AttachWeekly extends Attach{
    public AttachWeekly(){}
    @Builder
    public AttachWeekly(int id, String attachTitle, String thumb, Weekly weekly){
        this.id = id;
        this.attachTitle = attachTitle;
        this.thumbPath = thumb;
        this.weekly = weekly;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="WEEKLY_ID")
    private Weekly weekly;




}
