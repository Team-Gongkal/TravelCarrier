package tc.travelCarrier.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import tc.travelCarrier.domain.*;
import tc.travelCarrier.repository.WeeklyRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class WeeklyServiceTest {

    @Autowired WeeklyService weeklyService;
    @Autowired WeeklyRepository weeklyRepository;

    @Test
    @Rollback(false)
    public void 위클리_등록() throws Exception {
        //given
        Weekly weekly = new Weekly();
        User user = new User();
        user.setId(1);

        weekly.setUser(user);
        //weekly.setAttachWeekly();
        weekly.setNation("Korea");

        weekly.setTitle("Seoul");
        weekly.setText("완전 재밌었던 파리여행");
        weekly.setTravelDate(new TravelDate(
                new SimpleDateFormat("yyyy.MM.dd").parse("2020.01.20"),
                new SimpleDateFormat("yyyy.MM.dd").parse("2020.01.25")));
        weekly.setCrudDate(new CrudDate(new Date(), null));
        List<Gowith> gowiths = new ArrayList<>();
        gowiths.add(new Gowith(2));
        gowiths.add(new Gowith(3));
        weekly.setGowiths(gowiths);
        weekly.setStatus(OpenStatus.FOLLOW);

        //when
        int savedId = weeklyService.register(weekly);

        //then
        assertEquals(weekly, weeklyRepository.findOne(savedId));

    }
    @Test
    @Rollback(false)
    public void 나의_위클리_조회() throws Exception {
        //given
        User user = new User();
        user.setId(1);

        //when
        List<Weekly> weeklies = weeklyService.findWeeklies(user);

        //then
        assertEquals("맞는 위클리셋인지 확인 ",
                user.getId(), weeklies.get(0).getUser().getId());

    }
}