package tc.travelCarrier.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tc.travelCarrier.domain.*;
import tc.travelCarrier.dto.WeeklyDTO;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class AttachRepository {
    private final EntityManager em;


    // 위클리 사진 저장
    public void save(AttachWeekly attachWeekly){
        em.persist(attachWeekly);
    }

    // 데일리 사진 저장
    public void saveDailyPic(AttachDaily attachDaily){
        em.persist(attachDaily);
    }
    // 데일리 사진 검색
    public AttachDaily findAttachDaily(int id){
        AttachDaily at = em.find(AttachDaily.class, id);
        return at;
    }
    // 프로필 사진 저장
    public void saveProfilePic(AttachUser attachUser){
        em.persist(attachUser);
    }

    // 데일리 사진 삭제
    public void deleteById(int no) {
        AttachDaily attachDaily = em.find(AttachDaily.class, no);
        em.remove(attachDaily);
    }
    // 프로필 배경사진 저장
    public void saveBgPic(AttachUserBackground attachUserBackground){
        em.persist(attachUserBackground);
    }

    // 프로필 사진 수정 - 이전에 프사 있으면 수정후에 true를 반환, 프사 없었으면 새로 저장후 fasle를 반환함
    public AttachUser findAttachUser(AttachUser newAttachUser, User user){
        try{
            String jpql = "SELECT a FROM AttachUser a WHERE a.user = :user";
            AttachUser origin = em.createQuery(jpql, AttachUser.class)
                    .setParameter("user", user)
                    .getSingleResult();
            return origin;
        } catch (NoResultException e){
            return null;
        }
    }
    public void saveAttachUser(AttachUser attachUser){
        em.persist(attachUser);
    }

    // 배경 사진 수정
    public AttachUserBackground findBackground(AttachUserBackground aub, User user){
        try{
            String jpql = "SELECT a FROM AttachUserBackground a WHERE a.user = :user";
            AttachUserBackground origin = em.createQuery(jpql, AttachUserBackground.class)
                    .setParameter("user", user)
                    .getSingleResult();
            return origin;
        } catch (NoResultException e){
            return null;
        }
    }

    public void saveAttachUserBackground(AttachUserBackground attachUserBackground){
        em.persist(attachUserBackground);
    }


    public Map<String, List<String>> getRandomAttachThumbsByNation(int userId) {
        String sql = "WITH ranked_thumbs AS ("
                + "    SELECT a.nation_id, a.nation_name, f.attach_thumb, "
                + "           ROW_NUMBER() OVER(PARTITION BY a.nation_id ORDER BY RAND()) AS thumb_rank "
                + "    FROM nation a "
                + "    INNER JOIN weekly b ON a.nation_id = b.weekly_nation "
                + "    INNER JOIN daily c ON b.weekly_id = c.weekly_id "
                + "    INNER JOIN attach_daily e ON c.daily_id = e.daily_id "
                + "    INNER JOIN attach f ON e.attach_no = f.attach_no "
                + "    INNER JOIN user g ON g.USER_ID = b.user_id"
                + "    WHERE g.USER_ID = :userId "
                + ") "
                + "SELECT nation_name, attach_thumb "
                + "FROM ranked_thumbs "
                + "WHERE thumb_rank <= 5";

        Query query = em.createNativeQuery(sql);
        query.setParameter("userId", userId); // 파라미터 설정

        List<Object[]> results = query.getResultList();

        // Map을 생성하여 결과를 매핑합니다.
        Map<String, List<String>> resultMap = new HashMap<>();

        for (Object[] result : results) {
            String nationName = (String) result[0];
            String attachThumb = (String) result[1];

            // Map에 추가할 때 기존에 해당 nationId에 대한 List가 없으면 새로 생성합니다.
            resultMap.computeIfAbsent(nationName, k -> new ArrayList<>()).add(attachThumb);
        }

        return resultMap;
    }


}
