package tc.travelCarrier.domain;

import lombok.*;

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
    public AttachUser(User user, String attachTitle, String thumb) {
        this.user = user;
        this.attachTitle = attachTitle;
        this.thumbPath = thumb;
    }

    public void editAttachUser(AttachUser au){
        this.attachTitle = au.getAttachTitle();
        this.thumbPath = au.getFullThumbPath();
    }

}
