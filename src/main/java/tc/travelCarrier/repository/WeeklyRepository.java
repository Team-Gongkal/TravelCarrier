package tc.travelCarrier.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tc.travelCarrier.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class WeeklyRepository {

    private final EntityManager em;

    /**
     * 팔로워 리스트 조회
     * */
    public List<Follower> findFollowerList(User user) {
        String jpql = "select f from Follower f where f.user = :user";
        List<Follower> followerList = em.createQuery(jpql, Follower.class)
                    .setParameter("user", user)
                    .getResultList();
        return followerList;
    }

    /**
     * 새로운 위클리 저장
     * */
    public void save(Weekly weekly){;
        em.persist(weekly);
    }


    /**
     * 위클리 조회
     * */
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
