package tc.travelCarrier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tc.travelCarrier.domain.Notification;
import tc.travelCarrier.domain.User;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiver(User receiver);

    void deleteById(Long notificationId);

    List<Notification> findByReceiverAndIsRead(User user, boolean b);

}
