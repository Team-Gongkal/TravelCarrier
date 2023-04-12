package tc.travelCarrier.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import tc.travelCarrier.domain.*;
import tc.travelCarrier.dto.WeeklyForm;
import tc.travelCarrier.repository.MemberRepository;
import tc.travelCarrier.service.WeeklyService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static tc.travelCarrier.domain.Weekly.createWeekly;

@Controller
@RequiredArgsConstructor
@RequestMapping("/TravelCarrier")
public class WeeklyContoller {

    private final WeeklyService weeklyService;
    private final MemberRepository memberRepository;

    @GetMapping("/weeklyForm")
    public String getWeeklyForm(Model model) throws Exception {
        User user = memberRepository.getUser(1);
        System.out.println(user.getFollowers().get(0).getFollower().getAttachUser().getThumbPath());
        model.addAttribute("user", user);
        return "test/weekly_form";
    }


    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(OpenStatus.class, new OpenStatusEditor());
    }

    /**
     * 모달폼을 통해 위클리정보 저장
     * @param : WeeklyForm (폼정보)
     * @return : "redirect:/TravelCarrier/weekly/"+weeklyId
     * */
    @PostMapping(value="/weeklyForm")
    @ResponseBody
    public Integer regist(@Valid WeeklyForm form, BindingResult result,
                       @RequestParam("status") OpenStatus status) throws Exception {
/*        if(result.hasErrors()) {
            return "error";
        }*/
        User user = new User();
        user.setId(1);

        int weeklyId = weeklyService.register(form.getFile(),
                createWeekly(user, null, form.getTitle(), form.getNation(),
                new TravelDate(form.getSdate(),form.getEdate()), new CrudDate(new Date(),null),
                        status, form.getText(), form.getGowiths())
        );

        return weeklyId;
    }

    /**
     * 선택한 여행의 위클리를 조회
     * @param : weeklyId
     * @return :
     * */
    @GetMapping("/weekly/{weeklyId}")
    public String getWeekly(@PathVariable("weeklyId") int weeklyId, Model model) {
        Weekly weekly = weeklyService.findWeekly(weeklyId);
        model.addAttribute("weekly",weekly);
        System.out.println("위클이이이이이이이이이"+weekly.toString());
        System.out.println("txet"+weekly.getText());
        return "test/weekly";
    }

    /**
     * 키워드 저장
     * @param : List<String>
     * */
    @PostMapping("/weekly/{weekltId}/saveKeyword")
    public String saveKeyword(){

        return "";
    }


}
