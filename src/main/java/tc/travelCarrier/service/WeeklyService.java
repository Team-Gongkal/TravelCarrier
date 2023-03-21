package tc.travelCarrier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tc.travelCarrier.domain.Attach;
import tc.travelCarrier.domain.AttachWeekly;
import tc.travelCarrier.domain.Gowith;
import tc.travelCarrier.domain.Weekly;
import tc.travelCarrier.repository.WeeklyRepository;

import java.util.HashSet;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WeeklyService {

    private final WeeklyRepository weeklyRepository;

    /**
     * 위클리 등록
     */
    public int create (Weekly weekly) {
        weeklyRepository.save(weekly);
        //attach 저장하는거 필요함! 동행인 저장하는것도! <- 이거 weekly persist하면 cascade 되는건가??
        return weekly.getId();
    }


    /**
     * 위클리 조회
     */
    @Transactional(readOnly = true)
    public List<Weekly> findWeeklys() {
        return weeklyRepository.findAll();
    }
}
