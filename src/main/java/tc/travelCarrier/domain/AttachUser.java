package tc.travelCarrier.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ATTACH_USER")
@DiscriminatorValue("User")
@Getter @Setter
public class AttachUser extends Attach{
    public AttachUser(){}
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID")
    private User user;

    @Builder
    public AttachUser(User user, String title, String path) {
        this.user = user;
        this.attachTitle = title;
        this.thumbPath = path;
    }
    
}
