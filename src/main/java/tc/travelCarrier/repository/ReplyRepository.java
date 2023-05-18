package tc.travelCarrier.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tc.travelCarrier.domain.Reply;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ReplyRepository {
    private final EntityManager em;

    public int save(Reply reply){
        em.persist(reply);
        return reply.getId();
    }

    public Reply findReply(int replyId){
        return em.find(Reply.class, replyId);
    }

    public void remove(Reply reply) {
        em.remove(reply);
    }
}
