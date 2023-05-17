package tc.travelCarrier.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tc.travelCarrier.domain.Reply;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ReplyRepository {
    private final EntityManager em;

    public void save(Reply reply){
        em.persist(reply);
    }
}
