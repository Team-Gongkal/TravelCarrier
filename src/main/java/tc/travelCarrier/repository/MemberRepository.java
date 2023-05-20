package tc.travelCarrier.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tc.travelCarrier.domain.User;

import javax.persistence.EntityManager;

@Repository
public interface MemberRepository extends JpaRepository<User, Integer> {
/*    public User getUser(int userId){
        return em.find(User.class, userId);
    }*/

    public User findUserById(int userId);
    public User findUserByEmail(String email);
}
