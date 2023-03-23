package tc.travelCarrier.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tc.travelCarrier.domain.AttachWeekly;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class AttachRepository {
    private final EntityManager em;


    // 위클리 사진 저장
    public void save(AttachWeekly attachWeekly){
        em.persist(attachWeekly);
    }
}
