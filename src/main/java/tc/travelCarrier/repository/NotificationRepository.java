package tc.travelCarrier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tc.travelCarrier.domain.Notification;
import tc.travelCarrier.domain.User;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiver(User receiver);
}
