package tc.travelCarrier.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tc.travelCarrier.domain.Reply;
import tc.travelCarrier.domain.User;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter @Setter @ToString
public class ReplyDTO {
    public ReplyDTO(){}
    public ReplyDTO(int attachNo, String text, Date cdate, Date udate, Date ddate,
                    Reply originReply, User user, int replyId, User activeUser){
        this.attachNo = attachNo;
        this.text = text;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.cdate = format.format(cdate);
        if(udate != null) this.udate = format.format(udate);
        if(ddate != null) this.ddate = format.format(ddate);

        this.userName = user.getName();
        this.thumbPath = user.getAttachUser().getThumbPath();
        this.userId = user.getId();

        this.replyId = replyId;
        if(originReply != null){
            this.origin = originReply.getId();
            this.originName = originReply.getUser().getName();
        }

        if(user == activeUser) this.selfAuth = true;
        else this.selfAuth = false;
    }

    // 등록필드
    private int attachNo;
    private String text;
    private String cdate;
    private String udate;
    private int origin;

    //조회필드
    private String userName;
    private String thumbPath;
    private int userId;
    private int replyId;
    private String originName;

    //삭제필드
    private String ddate;

    //권한필드
    private boolean selfAuth;

}
