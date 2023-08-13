package tc.travelCarrier.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tc.travelCarrier.domain.*;
import tc.travelCarrier.dto.WeeklyDTO;

import javax.persistence.EntityManager;
import java.util.List;

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

    // 프로필 사진 삭제
    public void deleteById(int no) {
        AttachDaily attachDaily = em.find(AttachDaily.class, no);
        em.remove(attachDaily);
    }
    // 프로필 배경사진 저장
    public void saveBgPic(AttachUserBackground attachUserBackground){
        em.persist(attachUserBackground);
    }

    // 프로필 사진 수정
    public void editProfile(AttachUser newAttachUser, User user){
        String jpql = "SELECT a FROM AttachUser a WHERE a.user = :user";
        AttachUser origin = em.createQuery(jpql, AttachUser.class)
                .setParameter("user", user)
                .getSingleResult();
        em.remove(origin);

        em.persist(newAttachUser);
    }

}
