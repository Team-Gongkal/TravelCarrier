package tc.travelCarrier.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tc.travelCarrier.domain.AttachDaily;
import tc.travelCarrier.domain.Daily;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class DailyRepository {

    private final EntityManager em;

    /**
     * 데이터 가져오기
     * */
    public Daily getDaily(){
        em.find(AttachDaily.class, )
    }
}
