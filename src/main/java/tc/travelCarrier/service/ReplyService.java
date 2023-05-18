package tc.travelCarrier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tc.travelCarrier.domain.Reply;
import tc.travelCarrier.dto.ReplyDTO;
import tc.travelCarrier.repository.ReplyRepository;

import javax.persistence.EntityManager;
import java.text.ParseException;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    public int saveReply(Reply reply){
        return replyRepository.save(reply);
    }

    public void modifyReply(ReplyDTO dto) throws ParseException {
        Reply reply = replyRepository.findReply(dto.getReplyId());
        reply.modify(dto.getText(), dto.getUdate());
    }

    public void deleteReply(ReplyDTO dto) throws ParseException {
        Reply reply = replyRepository.findReply(dto.getReplyId());
        // 만약 자신에게 달린 답글이 있으면 getDdate()만 수정, 답글이 없으면 delete
        if(reply.getReplyList().size() == 0) {
            // 만약 삭제된 댓글의 답글을 삭제하는 연산이었을경우, 원댓도 삭제해야함
            Reply originReply;
            if(reply.getOrigin() != null) originReply = reply.getOrigin();
            else originReply = null;

            replyRepository.remove(reply);
            // originReply는 아직 플러쉬 전이므로 댓글리스트가 1개로 인식되는 상태임
            if(originReply != null && originReply.getReplyList().size() == 1) replyRepository.remove(originReply);
        }
        else reply.getCrudDate().setDdateReply(dto.getDdate());
    }

    public Reply getReply(int replyId) {
        return replyRepository.findReply(replyId);
    }
}