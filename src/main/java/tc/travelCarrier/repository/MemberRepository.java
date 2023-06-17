package tc.travelCarrier.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tc.travelCarrier.domain.Follower;
import tc.travelCarrier.domain.User;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<User, Integer> {
    public User findUserById(int userId);
    public User findUserByEmail(String email);

    @Query("SELECT u.followers FROM User u WHERE u = :user")
    public List<Follower> findFollowersByUser(@Param("user") User user);
}
