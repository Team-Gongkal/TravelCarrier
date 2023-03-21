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
import java.util.Date;

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
        weekly.setTitle("Paris");
        weekly.setNation("France");
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        weekly.setTravelDate(new TravelDate(f.parse("2022-10-20"),f.parse("2022-10-30")));
        weekly.setCrudDate(new CrudDate(new Date(), null));
        weekly.setStatus(OpenStatus.FOLLOW);
        weekly.setText("완전 재밌었던 파리여행");

        //when
        String savedId = weeklyService.create(weekly);

        //then
        assertEquals(weekly, weeklyRepository.findOne(savedId));

    }
    @Test
    public void 위클리_조회() throws Exception {
        //given

        //when

        //then

    }
}