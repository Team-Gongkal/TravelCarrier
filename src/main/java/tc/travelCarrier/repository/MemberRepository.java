package tc.travelCarrier.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tc.travelCarrier.domain.User;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;

    public User getUser(int userId){
        return em.find(User.class, userId);
    }
}
