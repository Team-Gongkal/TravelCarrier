package tc.travelCarrier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.domain.Weekly;
import tc.travelCarrier.repository.MemberRepository;
import tc.travelCarrier.repository.ReplyRepository;
import tc.travelCarrier.repository.WeeklySearchRepository;

import javax.persistence.EntityManager;
import org.springframework.data.domain.Pageable;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchService {
    private final WeeklySearchRepository weeklySearchRepository;
    private final EntityManager em;

    public Page<Weekly> findByTitleOrNationNameOrUserNameOrUserEmailContainingForCurrentUser (String keyword, User user, Pageable pageable){
        return weeklySearchRepository.findByTitleOrNationNameOrUserNameOrUserEmailContainingForCurrentUser(keyword, user, pageable);
    }

    public Page<Weekly> findWeeklyPaging (User user, Pageable pageable){
        return weeklySearchRepository.findByUserOrderByIdDesc(user, pageable);
    }
    public Page<Weekly> findTagWeeklyPaging (User user, Pageable pageable){
        return weeklySearchRepository.findTaggedWeekliesByUser(user, pageable);
    }

    public Page<Weekly> findTaggedWeekliesByKeywordAndUser(String keyword, User user, Pageable pageable) {
        return weeklySearchRepository.findTaggedWeekliesByKeywordAndUser(keyword, user, pageable);
    }
}
