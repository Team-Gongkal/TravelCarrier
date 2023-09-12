package tc.travelCarrier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import tc.travelCarrier.domain.Notification;
import tc.travelCarrier.domain.Reply;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.dto.ReplyDTO;
import tc.travelCarrier.repository.MemberRepository;
import tc.travelCarrier.repository.NotificationRepository;
import tc.travelCarrier.repository.ReplyRepository;
import tc.travelCarrier.sse.SseService;
import tc.travelCarrier.web.NotificationController;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;
    private final SseService sseService;
    private final EntityManager em;

    public int saveReply(Reply reply, User user) throws IOException {
        int replyId = replyRepository.save(reply);
        Notification no = notificationService.saveReplyNotification(reply, user);
        //알림전송
        if(no!=null) sseService.sendEmitter(no, no.getReceiver());
        return replyId;
    }



    public void modifyReply(ReplyDTO dto) throws ParseException {
        Reply reply = replyRepository.findReply(dto.getReplyId());
        reply.modify(dto.getText(), dto.getUdate());
    }

    public void deleteReply(ReplyDTO dto) throws ParseException {
        Reply reply = replyRepository.findReply(dto.getReplyId());
        // 답글이 없으면 delete, 답글이 있으면 getDdate()만 수정,
        if(reply.getReplyList()==null || reply.getReplyList().size() == 0) {
            // 만약 삭제된 댓글의 답글을 삭제하는 연산이었을경우, 원댓도 삭제해야함
            Reply originReply;
            if(reply.getOrigin() != null) originReply = reply.getOrigin();
            else originReply = null;

            replyRepository.remove(reply);
            em.flush();

            // 만약 삭제한 댓글의 원댓글이 삭제된 댓글일경우 DELTE쿼리 보내줌
            if(originReply != null
                    && originReply.getReplyList().size() == 0
                    && originReply.getCrudDate().getDdate() != null) replyRepository.remove(originReply);
        }
        else reply.getCrudDate().setDdateReply(dto.getDdate());
    }

    public Reply getReply(int replyId) {
        return replyRepository.findReply(replyId);
    }
}