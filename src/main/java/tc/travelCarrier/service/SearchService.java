package tc.travelCarrier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tc.travelCarrier.domain.Follower;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.domain.Weekly;
import tc.travelCarrier.dto.SearchDTO;
import tc.travelCarrier.repository.MemberRepository;
import tc.travelCarrier.repository.ReplyRepository;
import tc.travelCarrier.repository.WeeklySearchRepository;

import javax.persistence.EntityManager;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchService {
    private final WeeklySearchRepository weeklySearchRepository;
    private final EntityManager em;
    private final MemberRepository memberRepository;

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


    public Page<Follower> findFollowerByNameAndEmail(String keyword, User user, Pageable pageable) {
        return weeklySearchRepository.findFollowerByNameAndEmail(keyword, user, pageable);
    }

    public Page<Weekly> findWeeklyPagingByDate(SearchDTO searchDTO, User user, Pageable pageable) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date sdate = sdf.parse(searchDTO.getSdate());
        Date edate = sdf.parse(searchDTO.getEdate());
        return weeklySearchRepository.findWeeklyPagingByDate(sdate, edate, user, pageable);
    }

    public Page<Weekly> findTagWeeklyPagingByDate(SearchDTO searchDTO, User user, Pageable pageable) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date sdate = sdf.parse(searchDTO.getSdate());
        Date edate = sdf.parse(searchDTO.getEdate());
        return weeklySearchRepository.findTagWeeklyPagingByDate(sdate, edate,  user, pageable);
    }


}
