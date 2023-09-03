package tc.travelCarrier.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import tc.travelCarrier.dto.NotificationDTO;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Notification {

    public Notification(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Column
    private String title;
    @Column
    private String url;

    @Column
    private Date cdate;

    @Column(nullable = false)
    private Boolean isRead;

    @Column(nullable = false)
    private String notificationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver")
    private User receiver;

    @Builder
    public Notification(User sender, User receiver, String notificationType, String title, String url, Boolean isRead, Date cdate) {
        this.sender = sender;
        this.receiver = receiver;
        this.title = title;
        this.url = url;
        this.notificationType = notificationType;
        this.isRead = isRead;
        this.cdate = cdate;
    }

    public NotificationDTO changeJsonData(){
        NotificationDTO dto = NotificationDTO.builder()
                .id(id).type(notificationType).senderName(sender.getName())
                .senderThumbPath(sender.getAttachUser().getThumbPath()).isRead(false).url(url)
                .time(cdate).title(title!=null ? title :null).build();
        return dto;
    }




}
