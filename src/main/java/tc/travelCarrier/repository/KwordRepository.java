package tc.travelCarrier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tc.travelCarrier.domain.Kword;

public interface KwordRepository extends JpaRepository<Kword, Integer>{

    void deleteByDailyId(int dailyId);

}
