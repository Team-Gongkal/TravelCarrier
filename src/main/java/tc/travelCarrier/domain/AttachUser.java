package tc.travelCarrier.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ATTACH_USER")
@DiscriminatorValue("User")
@Getter @Setter
public class AttachUser extends Attach{

    @OneToOne
    @JoinColumn(name="USER_ID")
    private User user;
}
