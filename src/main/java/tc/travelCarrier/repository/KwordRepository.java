package tc.travelCarrier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tc.travelCarrier.domain.Kword;

@Repository
public interface KwordRepository extends JpaRepository<Kword, Integer>{

    void deleteByDailyId(int dailyId);

}
