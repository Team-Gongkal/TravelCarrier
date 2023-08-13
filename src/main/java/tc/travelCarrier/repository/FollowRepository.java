package tc.travelCarrier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tc.travelCarrier.domain.Follower;
import tc.travelCarrier.domain.User;

@Repository
public interface FollowRepository extends JpaRepository<Follower, Integer> {
}
