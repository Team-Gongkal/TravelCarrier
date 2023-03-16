package tc.travelCarrier.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ATTACH_DAILY")
@DiscriminatorValue("Daily")
@Getter @Setter
public class AttachDaily extends Attach{

    @ManyToOne
    @JoinColumn(name="DAILY_ID")
    private Daily daily;

    @Column(name = "ATTACH_DAILY_TITLE")
    private String title;

    @Column(name = "ATTACH_DAILY_TEXT")
    private String text;

    @Column(name = "ATTACH_DAILY_SORT")
    private int sort;
}
