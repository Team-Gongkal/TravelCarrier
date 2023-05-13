package tc.travelCarrier.repository;

import lombok.RequiredArgsConstructor;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tc.travelCarrier.domain.*;
import tc.travelCarrier.dto.DailyDTO;
import tc.travelCarrier.dto.KwordDTO;
import tc.travelCarrier.dto.WeeklyDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.SqlResultSetMapping;
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
    public List<WeeklyDTO> findWeeklyDto(int weeklyId){

        //dailyId, String dailyDate, String thumbPath, String realDate, String keywords
        String jpql =
                "SELECT new tc.travelCarrier.dto.WeeklyDTO(b.id, b.dailyDate, "+
                "a.thumbPath, d.travelDate.sDate) " +
                "FROM AttachDaily a " +
                "INNER JOIN a.daily b " +
                "INNER JOIN b.weekly d " +
                "WHERE a.thumb = 1 AND b.weekly.id = :weeklyId " +
                //"WHERE b.weekly.id = :weeklyId " +
                "GROUP BY b.id, b.dailyDate, a.thumbPath, d.travelDate.sDate";
        List<WeeklyDTO> result = em.createQuery(jpql, WeeklyDTO.class)
                .setParameter("weeklyId", weeklyId)
                .getResultList();

        String kwordsJpql =
                "SELECT k.text FROM Kword k WHERE k.daily.id = :dailyId";
        for(WeeklyDTO dto : result) {
            dto.setKeywords(em.createQuery(kwordsJpql, String.class)
                                .setParameter("dailyId", dto.getDailyId())
                    .getResultList()
            );
        }

        return result;
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


    public String saveKeyword(KwordDTO dto) {
        Daily d  = em.find(Daily.class, dto.getDailyId());
        for(String keyword : dto.getKwordList()) {
            Kword k = new Kword(d, keyword);
            em.persist(k);
        }
        return "success";
    }

    public void deleteGowith(Gowith gowith) {
        em.remove(gowith);
    }
    public void deleteAttachWeekly(AttachWeekly thumb) {
        em.remove(thumb);
    }
}
