package tc.travelCarrier.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tc.travelCarrier.domain.Attach;
import tc.travelCarrier.domain.Gowith;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.domain.Weekly;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class WeeklyRepository {

    private final EntityManager em;

    /**
     * 새로운 위클리 저장
     * */
    public void save(Weekly weekly){
        em.persist(weekly);
    }

    public Weekly findOne(int weeklyId){
        Weekly weekly =  em.find(Weekly.class, weeklyId);
        return weekly;
    }

    /**
     * 현재 로그인한 user가 작성한 모든 weekly 조회
     * */
    public List<Weekly> findByUserId(User user){
        return em.createQuery("select w from Weekly w where w.user = : user",
                        Weekly.class)
                .setParameter("user", user)
                .getResultList();
    }
}
