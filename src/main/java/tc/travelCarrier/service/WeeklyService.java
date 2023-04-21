package tc.travelCarrier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tc.travelCarrier.domain.*;
import tc.travelCarrier.dto.KwordDTO;
import tc.travelCarrier.dto.WeeklyDTO;
import tc.travelCarrier.repository.AttachRepository;
import tc.travelCarrier.repository.DailyRepository;
import tc.travelCarrier.repository.KwordRepository;
import tc.travelCarrier.repository.WeeklyRepository;

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

    @Value("${file.dir}")
    private String fileDir;

    /**
     * 팔로워 목록 조회
     * */
    public List<Follower> getFollowerList(User user) throws Exception {
        return weeklyRepository.findFollowerList(user);
    }

    /**
     * 위클리 등록
     */
    public int register(MultipartFile file, Weekly weekly) throws Exception {
        weeklyRepository.save(weekly);
        attachService.saveAttachWeekly(file,weekly);
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
    /**
     * 위클리 삭제
     */

    // 위클리 키워드 저장
    public String saveKeyword(KwordDTO dto){
        kwordRepository.deleteByDailyId(dto.getDailyId());
        Daily daily = dailyRepository.findDaily(dto.getDailyId());
        for(String kword : dto.getKwordList()){
            kwordRepository.save(new Kword(daily,kword));
        }
        return "success";
    }
}
