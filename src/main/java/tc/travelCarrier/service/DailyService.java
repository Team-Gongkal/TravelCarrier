package tc.travelCarrier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tc.travelCarrier.domain.AttachDaily;
import tc.travelCarrier.domain.Daily;
import tc.travelCarrier.domain.Weekly;
import tc.travelCarrier.dto.DailyDTO;
import tc.travelCarrier.repository.DailyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DailyService {

    private final DailyRepository dailyRepository;

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

}
