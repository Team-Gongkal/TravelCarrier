package tc.travelCarrier.domain;

import lombok.Getter;
import lombok.Setter;
import tc.travelCarrier.dto.ReplyDTO;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Entity
@Getter @Setter
public class Reply {
    public Reply(){}
    public Reply(AttachDaily ad, String text, User user, CrudDate cd, Reply origin){
        //새댓글 등록
        this.attachDaily = ad;
        this.text = text;
        this.user = user;
        this.crudDate = cd;
        this.origin = origin;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="REPLY_ID")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ATTACH_NO")
    private AttachDaily attachDaily;

    @Column(name="REPLY_TEXT")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID")
    private User user;

    @Embedded
    private CrudDate crudDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPLY_ORIGIN")
    private Reply origin;

    @OneToMany(mappedBy = "origin")
    private List<Reply> replyList;


    public void modify(String text, String udate) throws ParseException {
        this.text = text;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        this.crudDate.setUdate(formatter.parse(udate));
    }
}
