package tc.travelCarrier.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tc.travelCarrier.domain.Follower;
import tc.travelCarrier.domain.OpenStatus;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.domain.Weekly;

import org.springframework.data.domain.Pageable;

import java.util.Date;
@Repository
public interface WeeklySearchRepository extends JpaRepository<Weekly, Integer> {
    @Query("SELECT DISTINCT w FROM Weekly w JOIN w.nation n JOIN w.gowiths g JOIN g.user u WHERE (w.title LIKE %?1% OR n.name LIKE %?1% OR u.email LIKE %?1% OR u.name LIKE %?1%) AND w.user = ?2")
    Page<Weekly> findByTitleOrNationNameOrUserNameOrUserEmailContainingForCurrentUser(String keyword, User user, Pageable pageable);
    @Query("SELECT DISTINCT w FROM Weekly w JOIN w.nation n JOIN w.gowiths g JOIN g.user u WHERE (w.title LIKE %?1% OR n.name LIKE %?1% OR u.email LIKE %?1% OR u.name LIKE %?1%) AND g.user = ?2")
    Page<Weekly> findTaggedWeekliesByKeywordAndUser(String keyword, User user, Pageable pageable);


    Page<Weekly> findByUserOrderByIdDesc(User user, Pageable pageable);



    @Query("SELECT w FROM Weekly w INNER JOIN w.gowiths g WHERE g.user = ?1 ORDER BY w.id DESC")
    Page<Weekly> findTaggedWeekliesByUser(User user, Pageable pageable);



    @Query("SELECT f FROM Follower f JOIN f.user u WHERE u = ?2 AND (f.follower.name LIKE %?1% OR f.follower.email LIKE %?1%)")
    Page<Follower> findFollowerByNameAndEmail (String keyword, User user, Pageable pageable);

    @Query("SELECT w FROM Weekly w WHERE w.user = ?3 AND (w.travelDate.sDate >= ?1 AND w.travelDate.eDate <= ?2)")
    Page<Weekly> findWeeklyPagingByDate(Date sdate, Date edate, User user, Pageable pageable);
    @Query("SELECT DISTINCT w FROM Weekly w JOIN w.gowiths g JOIN g.user u WHERE g.user = ?3 AND (w.travelDate.sDate >= ?1 AND w.travelDate.eDate <= ?2)")
    Page<Weekly> findTagWeeklyPagingByDate(Date sdate, Date edate, User user, Pageable pageable);



    // 친구의 마이페이지

    // 팔로우되어있지 않은 상대의 마이페이지 -> ALL만
    @Query("SELECT w FROM Weekly w WHERE w.user = ?1 AND w.status = ?2 ORDER BY w.id DESC")
    Page<Weekly> findNotFollowWeekliesByTraveler(User user, OpenStatus status, Pageable pageable);

    // 팔로우되어있는 상대의 마이페이지 -> FOLLOW+ALL만 즉 Not ME
    @Query("SELECT w FROM Weekly w WHERE w.user = ?1 AND w.status != ?2 ORDER BY w.id DESC")
    Page<Weekly> findFollowWeekliesByTraveler(User user, OpenStatus status, Pageable pageable);

    // 친구의 마이페이지 태그된 글 목록 -> 해당 글의 글쓴이가 로그인유저의 팔로워일때 AND ALL인것만 뜸
}
