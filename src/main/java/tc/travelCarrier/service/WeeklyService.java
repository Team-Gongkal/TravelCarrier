package tc.travelCarrier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tc.travelCarrier.domain.*;
import tc.travelCarrier.repository.AttachRepository;
import tc.travelCarrier.repository.WeeklyRepository;

import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor
public class WeeklyService {

    private final WeeklyRepository weeklyRepository;
    private final AttachRepository attachRepository;
    private final AttachService attachService;

    @Value("${file.dir}")
    private String fileDir;


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

    /**
     * 위클리 수정
     */
    /**
     * 위클리 삭제
     */
}
