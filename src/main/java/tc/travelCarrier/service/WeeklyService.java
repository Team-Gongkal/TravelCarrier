package tc.travelCarrier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tc.travelCarrier.domain.*;
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
    public int register(Weekly weekly) {
        weeklyRepository.save(weekly);
        return weekly.getId();
    }


    /**
     * 위클리 조회
     */
    @Transactional(readOnly = true)
    public List<Weekly> findWeeklies(User user) {
        return weeklyRepository.findByUserId(user);
    }


    /**
     * 위클리 수정
     */
    /**
     * 위클리 삭제
     */
}
