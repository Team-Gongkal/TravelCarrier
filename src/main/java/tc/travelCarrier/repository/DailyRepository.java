package tc.travelCarrier.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tc.travelCarrier.domain.AttachDaily;
import tc.travelCarrier.domain.Daily;
import tc.travelCarrier.domain.Weekly;
import tc.travelCarrier.dto.DailyDTO;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DailyRepository {

    private final EntityManager em;

    /**
     * 해당 위클리의 모든 데일리 데이터 가져오기
     * */
    public List<DailyDTO> getAttachDaily(Weekly weekly){
        String jpql = "select new tc.travelCarrier.dto.DailyDTO(a.id, b.dailyDate, a.sort, a.title, a.text, a.thumbPath, a.isThumb)"
                + " from AttachDaily a"
                + " inner join a.daily b"
                + " where b.weekly = :weekly";
        List<DailyDTO> result = em.createQuery(jpql, DailyDTO.class)
                .setParameter("weekly", weekly)
                .getResultList();
        return result;
    }

    /**
     * 데일리폼을 저장하는 메소드
     * */
    public void save(AttachDaily attachDaily){
        em.persist(attachDaily);
    }

    // 데일리 아이디 검색
    public Daily findByWeeklyAndDailyDate(Weekly weekly, String dailyDate){
        String jpql = "select a from Daily a"
                + " where a.weekly = :weekly"
                + " and a.dailyDate = :dailyDate";
        return em.createQuery(jpql, Daily.class)
              .setParameter("weekly", weekly)
              .setParameter("dailyDate", dailyDate)
              .getSingleResult();
    }
    // 데일리 등록
    public Daily saveNewDaily(Daily daily){
        em.persist(daily);
        em.flush();
        return daily;
    }

}
