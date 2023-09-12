package tc.travelCarrier.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.domain.Weekly;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter @Setter @ToString
public class NotificationDTO implements Comparable<NotificationDTO>{
    // sender와 receiver의 thumbPath,닉네임
    // id와 isread와 content까지
    // 시간, 제목, url(id)도
    public Long id;
    public String type;

    public String senderName;
    public String senderThumbPath;

    public Boolean isRead;
    public String url;
    public String title;

    public String time;

    @Builder
    public NotificationDTO(Long id, String type, String senderName, String senderThumbPath,
                           Boolean isRead, String url, Date time, String title) {
        this.id = id;
        this.type = type;
        this.senderName = senderName;
        this.senderThumbPath = senderThumbPath;
        this.isRead = isRead;
        this.url = url;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        this.time = dateFormat.format(time);
        this.title = title;
    }


    //알림발생일 최신순으로 정렬
    @Override
    public int compareTo(NotificationDTO o) {
        if(this.id > o.id) return -1;
        else if(this.id<o.id) return 1;
        else return 0;
    }
}
