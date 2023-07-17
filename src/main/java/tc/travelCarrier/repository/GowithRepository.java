package tc.travelCarrier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tc.travelCarrier.domain.Gowith;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.domain.Weekly;

public interface GowithRepository extends JpaRepository<Gowith, Integer> {

    Gowith findByWeeklyAndUser(Weekly weekly, User user);
}
