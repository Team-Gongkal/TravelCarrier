package tc.travelCarrier.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import tc.travelCarrier.domain.*;
import tc.travelCarrier.dto.KwordDTO;
import tc.travelCarrier.dto.WeeklyDTO;
import tc.travelCarrier.dto.WeeklyForm;
import tc.travelCarrier.repository.MemberRepository;
import tc.travelCarrier.service.WeeklyService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

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
    @PostMapping("/weekly/saveKeyword")
    @ResponseBody
    public String saveKeywords(@RequestBody KwordDTO dto) throws Exception {
        return weeklyService.saveKeyword(dto);
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
        if(result.hasErrors()) {
            System.out.println("Validation Error");
        }
        User user = new User();
        user.setId(1);
        int weeklyId = weeklyService.register(form.getFile(),
                createWeekly(user, null, form.getTitle(), form.getNation(),
                new TravelDate(form.getSdate(),form.getEdate()), new CrudDate(new Date(),null),
                        status, form.getText().replace("\n", "\\n"), form.getGowiths())
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

        long period = ((weekly.getTravelDate().getEDate().getTime() - weekly.getTravelDate().getSDate().getTime()) / 1000)/ (24*60*60)+1;
        //model.addAttribute("period", period);

        System.out.println("wwww"+weekly.toString()+", period : "+period);
        // 각 데일리의 대표사진과 키워드들을 가져와야함, 그래프로 찾으면 탐색이 길어져 비효율적
        // 따라서 dto를 만들어서 넣을것임
        List<WeeklyDTO> wdList = weeklyService.findWeeklyDto(weeklyId);

        Date sDate = weekly.getTravelDate().getSDate();
        Calendar baseCal = Calendar.getInstance();
        baseCal.setTime(sDate);

        //period만큼 비어있는 DAY 포함해주기
        List<WeeklyDTO> allWdList = new ArrayList<>();
        for(int p=1; p<=period; p++){
            boolean flag = false;
            for(WeeklyDTO wd : wdList){
                if(wd.getDailyDate().equals("DAY"+p)) {
                    allWdList.add(wd);
                    flag = true;
                    break;
                }
            }
            if(!flag) {
                Calendar cal = (Calendar) baseCal.clone();
                cal.add(Calendar.DATE, p-1);
                allWdList.add(new WeeklyDTO(p, cal.getTime()));
            }
        }


        for(WeeklyDTO wd : allWdList){
            System.out.println("allWdList : "+wd.toString());
        }
        model.addAttribute("allWdList", allWdList);
        return "test/weekly";
    }

}
