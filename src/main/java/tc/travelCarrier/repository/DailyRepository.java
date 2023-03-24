package tc.travelCarrier.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tc.travelCarrier.domain.AttachDaily;
import tc.travelCarrier.domain.Daily;
import tc.travelCarrier.domain.Weekly;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DailyRepository {

    private final EntityManager em;

    /**
     * 해당 위클리의 모든 데일리 데이터 가져오기
     * */
    public AttachDaily getAttachDaily(Daily daily){
        AttachDaily answer = (AttachDaily) em.createQuery("select a from AttachDaily a where a.daily = :dailyId",
                        AttachDaily.class)
                .setParameter("dailyId",daily)
               .getSingleResult();
        return answer;
    }

    /**
     *
     * */

}
