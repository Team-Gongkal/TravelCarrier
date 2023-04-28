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
        String jpql = "select new tc.travelCarrier.dto.DailyDTO(a.id, b.dailyDate, a.sort, a.title, a.text, a.thumbPath, a.thumb)"
                + " from AttachDaily a"
                + " inner join a.daily b"
                + " where b.weekly = :weekly";
        List<DailyDTO> result = em.createQuery(jpql, DailyDTO.class)
                .setParameter("weekly", weekly)
                .getResultList();
        return result;
    }



    public Daily findDaily(int dailyId){
        return em.find(Daily.class, dailyId);
    }

    public AttachDaily findAttachDaily(int attachId){
        return em.find(AttachDaily.class, attachId);
    }

}
