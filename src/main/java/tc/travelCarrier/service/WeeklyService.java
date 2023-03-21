package tc.travelCarrier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tc.travelCarrier.domain.Weekly;
import tc.travelCarrier.repository.WeeklyRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WeeklyService {

    private final WeeklyRepository weeklyRepository;

    /**
     * 위클리 등록
     */
    public String create (Weekly weekly) {
        weeklyRepository.save(weekly);
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
