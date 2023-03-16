package tc.travelCarrier.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ATTACH_WEEKLY")
@DiscriminatorValue("Weekly")
@Getter @Setter
public class AttachWeekly extends Attach{

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="WEEKLY_ID")
    private Weekly weekly;

}
