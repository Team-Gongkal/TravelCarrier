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

}
