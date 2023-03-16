package tc.travelCarrier.domain;

import javax.persistence.*;

@Entity
@Table(name = "ATTACH_DAILY")
@DiscriminatorValue("Daily")
public class AttachDaily extends Attach{

    @ManyToOne
    private Daily daily;

    @Column(name = "ATTACH_DAILY_TITLE")
    private String title;

    @Column(name = "ATTACH_DAILY_TEXT")
    private String text;

    @Column(name = "ATTACH_DAILY_SORT")
    private int sort;
}
