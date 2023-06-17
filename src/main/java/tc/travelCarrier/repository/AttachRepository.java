package tc.travelCarrier.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tc.travelCarrier.domain.AttachDaily;
import tc.travelCarrier.domain.AttachUser;
import tc.travelCarrier.domain.AttachUserBackground;
import tc.travelCarrier.domain.AttachWeekly;

import javax.persistence.EntityManager;

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
    // 프로필 사진 저장
    public void saveBgPic(AttachUserBackground attachUserBackground){
        em.persist(attachUserBackground);
    }



}
