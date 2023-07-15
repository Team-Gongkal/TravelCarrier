package tc.travelCarrier.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tc.travelCarrier.domain.Follower;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.domain.Weekly;

import org.springframework.data.domain.Pageable;

public interface WeeklySearchRepository extends JpaRepository<Weekly, Integer> {
    @Query("SELECT DISTINCT w FROM Weekly w JOIN w.nation n JOIN w.gowiths g JOIN g.user u WHERE (w.title LIKE %?1% OR n.name LIKE %?1% OR u.email LIKE %?1% OR u.name LIKE %?1%) AND w.user = ?2")
    Page<Weekly> findByTitleOrNationNameOrUserNameOrUserEmailContainingForCurrentUser(String keyword, User user, Pageable pageable);
    @Query("SELECT DISTINCT w FROM Weekly w JOIN w.nation n JOIN w.gowiths g JOIN g.user u WHERE (w.title LIKE %?1% OR n.name LIKE %?1% OR u.email LIKE %?1% OR u.name LIKE %?1%) AND g.user = ?2")
    Page<Weekly> findTaggedWeekliesByKeywordAndUser(String keyword, User user, Pageable pageable);


    Page<Weekly> findByUserOrderByIdDesc(User user, Pageable pageable);

    @Query("SELECT w FROM Weekly w INNER JOIN w.gowiths g WHERE g.user = ?1 AND g.hide = false ORDER BY w.id DESC")
    Page<Weekly> findTaggedWeekliesByUser(User user, Pageable pageable);

    @Query("SELECT f FROM Follower f WHERE f.user = ?1")
    Page<Follower> findMyFollower (User user, Pageable pageable);

    @Query("SELECT f FROM Follower f JOIN f.user u WHERE u = ?2 AND (f.follower.name LIKE %?1% OR f.follower.email LIKE %?1%)")
    Page<Follower> findFollowerByNameAndEmail (String keyword, User user, Pageable pageable);



}
