package tc.travelCarrier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tc.travelCarrier.domain.*;
import tc.travelCarrier.dto.KwordDTO;
import tc.travelCarrier.dto.WeeklyDTO;
import tc.travelCarrier.dto.WeeklyForm;
import tc.travelCarrier.exeption.AuthenticationException;
import tc.travelCarrier.repository.*;
import tc.travelCarrier.sse.SseService;

import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor
public class WeeklyService {

    private final WeeklyRepository weeklyRepository;
    private final DailyRepository dailyRepository;
    private final KwordRepository kwordRepository;
    private final AttachRepository attachRepository;
    private final AttachService attachService;
    private final MemberRepository memberRepository;
    private final GowithRepository gowithRepository;
    private final NotificationService notificationService;
    private final NationRepository nationRepository;
    private final SseService sseService;

    /**
     * 팔로워 목록 조회
     * */
    public List<Follower> getFollowerList(User user) throws Exception {
        return weeklyRepository.findFollowerList(user);
    }

    /**
     * 위클리 등록
     */
    public int register(MultipartFile file, Weekly weekly, User user) throws Exception {
        // 위클리 정보 저장
        weeklyRepository.save(weekly);
        // 파일저장
        attachService.saveAttachWeekly(file, weekly);
        // 알림전송
        if(weekly.getGowiths().size()!=0) {
            Notification noti[] = notificationService.saveTagNotification(weekly, user);
            for(Notification n : noti){
                sseService.sendEmitter(n, n.getReceiver());
            }
        }

        return weekly.getId();
    }


    /**
     * 위클리 목록조회
     */
    @Transactional(readOnly = true)
    public List<Weekly> findWeeklies(User user) {
        return weeklyRepository.findByUserId(user);
    }

    /**
     * 위클리 내용조회
     * */
    public Weekly findWeekly(int weeklyId) { return weeklyRepository.findOne(weeklyId);}
    public List<WeeklyDTO> findWeeklyDto(int weeklyId){
        return weeklyRepository.findWeeklyDto(weeklyId);
    }

    /**
     * 위클리 수정
     */
    public void updateWeekly(int weeklyId, WeeklyForm form, OpenStatus status) throws Exception {
        // 위클리 정보 수정
        Weekly weekly = findWeekly(weeklyId);
        Nation nation = nationRepository.findNationById(form.getNation());
        weekly.updateWeekly(form, status, nation);

        // 위클리 동행인 삭제
        for (Gowith gowith : weekly.getGowiths()) {
            weeklyRepository.deleteGowith(gowith);
        }
        weekly.getGowiths().clear();
        // 위클리 동행인 변경
        if(form.getGowiths() != null) {
            for (int id : form.getGowiths()) {
                User user = memberRepository.findUserById(id);
                weekly.updateGowith(user);
            }
        }
        // 위클리 썸네일 변경
        //weeklyRepository.deleteAttachWeekly(weekly.getAttachWeekly());
        //weekly.setAttachWeekly(null);
        attachService.saveUpdateAttachWeekly(form, weekly);

    }

    // 위클리 키워드 저장
    public String saveKeyword(KwordDTO dto){
        kwordRepository.deleteByDailyId(dto.getDailyId());
        Daily daily = dailyRepository.findDaily(dto.getDailyId());
        for(String kword : dto.getKwordList()){
            kwordRepository.save(new Kword(daily,kword));
        }
        return "success";
    }

    // 위클리 삭제
    public void deleteWeekly(Weekly weekly, User user) {
        // 로그인 user가 글쓴이가 맞는지 확인 후 delete처리한다.
        if(weekly.getUser().getId() == user.getId()) {
            //삭제 전에 서버에 있는 모든 데이터들을 삭제해줘야함
            
            //1. 모든 데일리 삭제
            for (Daily d : weekly.getDailys()){
                for(AttachDaily at : d.getAttachDailies()){
                    attachService.deleteServerFile(at.getFullThumbPath());
                }
            }
            //2. 위클리 썸네일 삭제
            attachService.deleteServerFile(weekly.getAttachWeekly().getFullThumbPath());

            //3. 엔티티 삭제
            weeklyRepository.remove(weekly);
        }
        else throw new AuthenticationException("deleteWeekly",user.getEmail()+"은 "+weekly.getTitle()+"의 글쓴이인 "+weekly.getUser().getEmail()+"가 아닙니다");
    }

    // 위클리 숨김/보이기처리
    public void hideOrShowWeekly(Weekly weekly, String type, User user) {
        Gowith gowith = gowithRepository.findByWeeklyAndUser(weekly, user);
        if(type.equals("hide")) gowith.setHide(true);
        else if(type.equals("show")) gowith.setHide(false);
        gowithRepository.save(gowith); // 객체 변경사항 저장
    }
}
