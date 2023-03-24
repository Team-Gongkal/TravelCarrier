package tc.travelCarrier.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tc.travelCarrier.domain.*;
import tc.travelCarrier.service.WeeklyService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;

import static tc.travelCarrier.domain.Weekly.createWeekly;

@Controller
@RequiredArgsConstructor
public class WeeklyContoller {

    private final WeeklyService weeklyService;

    @GetMapping("/weeklyForm")
    public String getWeeklyForm(Model model){
        model.addAttribute("weeklyForm", new WeeklyForm());
        return "page/weekly_form_css";
    }

    /**
     * 모달폼을 통해 위클리정보 저장
     * @param : WeeklyForm (폼정보)
     * @return : "weekly"+weeklyId (생성된 위클리의 ID)
     * */
    @PostMapping(value="/weeklyForm")
    public String regist(@Valid WeeklyForm form, BindingResult result) throws IOException {
        HashSet<Integer> tmp= new HashSet<>();
        tmp.add(2);
        tmp.add(3);
        form.setGowiths(tmp);

/*        if(result.hasErrors()) {
            return "error";
        }*/

        TravelDate tdate = new TravelDate(form.getSdate(),form.getEdate());

        User user = new User();
        user.setId(1);

        OpenStatus status = OpenStatus.ALL;;
        switch(form.getStatus()) {
            case "public" :
                status = OpenStatus.ALL;
                break;
            case "shareFriends" :
                status = OpenStatus.FOLLOW;
                break;
            case "private" :
                status = OpenStatus.ME;
        }



        int weeklyId = weeklyService.register(form.getFile(),
                createWeekly(user, null, form.getTitle(), form.getNation(),
                tdate, new CrudDate(new Date(),null), status, form.getText(), form.getGowiths())
        );

        return "page/main01";
    }

    /**
     * 선택한 여행의 위클리를 조회
     * @param : weeklyId
     * @return : DailyForm???
     * */
    @GetMapping("/weekly/{weeklyId}")
    public String home() {

        return "map01";
    }
}
