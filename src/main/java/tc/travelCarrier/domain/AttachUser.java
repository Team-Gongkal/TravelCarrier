package tc.travelCarrier.domain;

import javax.persistence.*;

@Entity
@Table(name = "ATTACH_USER")
@DiscriminatorValue("User")
public class AttachUser extends Attach{

    @OneToOne
    @JoinColumn(name="USER_ID")
    private User user;
}
