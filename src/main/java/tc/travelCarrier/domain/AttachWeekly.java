package tc.travelCarrier.domain;

import javax.persistence.*;

@Entity
@Table(name = "ATTACH_DAILY")
@DiscriminatorValue("Weekly")
public class AttachWeekly extends Attach{

    @OneToOne
    @JoinColumn(name="WEEKLY_ID")
    private Weekly weekly;

}
