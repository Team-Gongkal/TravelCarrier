package tc.travelCarrier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tc.travelCarrier.domain.Reply;
import tc.travelCarrier.repository.ReplyRepository;

import javax.persistence.EntityManager;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    public void saveReply(Reply reply){
        replyRepository.save(reply);
    }

}
