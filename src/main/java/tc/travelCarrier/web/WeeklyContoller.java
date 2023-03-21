package tc.travelCarrier.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tc.travelCarrier.domain.*;
import tc.travelCarrier.service.WeeklyService;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashSet;

@Controller
@RequiredArgsConstructor
public class WeeklyContoller {

    private final WeeklyService weeklyService;

    @GetMapping("/weeklyForm")
    public String getWeeklyForm(Model model){
        model.addAttribute("weeklyForm", new WeeklyForm());
        return "weekly-form";
    }

    /**
     * 모달폼을 통해 위클리정보 저장
     * @param : WeeklyForm (폼정보)
     * @return : "weekly"+weeklyId (생성된 위클리의 ID)
     * */
    @PostMapping(value="/weekly/regist")
    public String regist(@Valid WeeklyForm form, BindingResult result){

        if(result.hasErrors()) {
            return "error";
        }

        TravelDate date = new TravelDate(form.getSdate(),form.getEdate());

        User user = new User();
        user.setId(1);

        OpenStatus status = OpenStatus.ALL;;
        switch(form.getStatus()) {
            case "1" :
                status = OpenStatus.ALL;
                break;
            case "2" :
                status = OpenStatus.FOLLOW;
                break;
            case "3" :
                status = OpenStatus.ME;
        }

        if(form.getStatus().equals("2")) status = OpenStatus.FOLLOW;
        else if(form.getStatus().equals("3")) status = OpenStatus.ME;

        //첨부파일 처리

        //gowith set처리


/*
        Weekly weekly = new Weekly(user, new AttachWeekly(),form.getTitle(), form.getNation(), date,
                new CrudDate(new Date(), null), status, form.getText(), form.getGowiths());
*/

       // int weeklyId = weeklyService.create(weekly);
        int weeklyId = 1;
        return "/weekly/"+weeklyId;
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
