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
    @PostMapping(value="/weeklyForm/regist")
    public String regist(@Valid WeeklyForm form, BindingResult result){


        System.out.println("==============================");
        System.out.println("썸네일 : "+form.getThumbnail());
        System.out.println("국가 : "+form.getNation());
        System.out.println("출국일 : "+form.getSdate());
        System.out.println("입국일 : "+form.getEdate());
        System.out.println("제목 : "+form.getTitle());
        System.out.println("본문 : "+form.getText());
        System.out.println("공개여부 : "+form.getStatus());
        for(int i : form.getGowiths()) System.out.println("동행인 : "+i);
        System.out.println("==============================");

        if(result.hasErrors()) {
            return "error";
        }

        TravelDate tdate = new TravelDate(form.getSdate(),form.getEdate());

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

        System.out.println("ㅅㅂ");

        Weekly weekly = Weekly.createWeekly(user,new AttachWeekly(), form.getTitle(), form.getNation(),
                tdate, new CrudDate(new Date(),null), status, form.getText(), form.getGowiths());

        System.out.println("위클리만듦");

        int weeklyId = weeklyService.register(weekly);
        System.out.println("성공!");
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
