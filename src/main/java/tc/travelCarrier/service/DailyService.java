package tc.travelCarrier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tc.travelCarrier.domain.AttachDaily;
import tc.travelCarrier.domain.Daily;
import tc.travelCarrier.domain.Weekly;
import tc.travelCarrier.dto.DailyDTO;
import tc.travelCarrier.repository.DailyRepository;
import tc.travelCarrier.repository.WeeklyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DailyService {

    private final DailyRepository dailyRepository;
    private final WeeklyRepository weeklyRepository;

    /**
     * 해당 위클리의 데일리정보 가져오는 메소드
     * */
    public List<DailyDTO> getAttachDaily(Weekly weekly){
        return dailyRepository.getAttachDaily(weekly);
    }

    /**
     * 데일리폼을 저장하는 메소드
     * */
    public void save(AttachDaily attachDaily){
       // 등록한 데일리정보를 DB에 저장
        dailyRepository.save(attachDaily);

    }

    //데일리 있으면 find, 없으면 등록후 find
    public Daily getDaily(int weeklyId, String dailyDate){
        Weekly weekly = weeklyRepository.findOne(weeklyId);
        Daily daily =  dailyRepository.findByWeeklyAndDailyDate(weekly,dailyDate);
        if(daily == null){
            System.out.println("존재하지 않던 데일리이므로 새로 생성!!!");
            Daily newDaily = new Daily();
            newDaily.setWeekly(weekly);
            newDaily.setDailyDate(dailyDate);
            daily = dailyRepository.saveNewDaily(newDaily);
        }
        System.out.println("지금 데일리 아이디는 : " + daily.getId());
        return daily;
    }
}
