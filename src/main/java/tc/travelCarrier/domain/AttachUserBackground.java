package tc.travelCarrier.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ATTACH_USER_BG")
@DiscriminatorValue("BG")
@Getter  @Setter
public class AttachUserBackground extends Attach{
    public AttachUserBackground(){}

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID")
    private User user;

    @Builder
    public AttachUserBackground(User user, String title, String path) {
        this.user = user;
        this.attachTitle = title;
        this.thumbPath = path;
    }

    public void editAttachUserBackground(AttachUserBackground aub) {
        this.attachTitle = aub.getAttachTitle();
        this.thumbPath = aub.getThumbPath();
    }
}