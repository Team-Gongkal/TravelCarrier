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
        reply.getCrudDate().setDdateReply(dto.getDdate());
    }

    public Reply getReply(int replyId) {
        return replyRepository.findReply(replyId);
    }
}