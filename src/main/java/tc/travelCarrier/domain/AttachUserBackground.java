package tc.travelCarrier.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ATTACH_USER_BG")
@DiscriminatorValue("BG")
@Getter  @Setter
public class AttachUserBackground extends Attach{
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID")
    private User user;
}