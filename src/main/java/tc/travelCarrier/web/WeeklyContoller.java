package tc.travelCarrier.web;

import java.util.ArrayList;
import java.util.Calendar;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import tc.travelCarrier.auth.PrincipalDetails;
import tc.travelCarrier.domain.CrudDate;
import tc.travelCarrier.domain.OpenStatus;
import tc.travelCarrier.domain.TravelDate;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.domain.Weekly;
import tc.travelCarrier.dto.KwordDTO;
import tc.travelCarrier.dto.WeeklyDTO;
import tc.travelCarrier.dto.WeeklyForm;
import tc.travelCarrier.repository.MemberRepository;
import tc.travelCarrier.service.WeeklyService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/TravelCarrier")
public class WeeklyContoller {

    private final WeeklyService weeklyService;
    private final MemberRepository memberRepository;

    @GetMapping("/weeklyForm")
    public String getWeeklyForm(Model model,@AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        //로그인한 유저의 정보
        User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());
        model.addAttribute("user", user);

        return "test/weekly_form";
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(OpenStatus.class, new OpenStatusEditor());
    }

    @PostMapping(value="/weeklyForm")
    @ResponseBody
    public Integer regist(@Valid WeeklyForm form, BindingResult result, @RequestParam("status") OpenStatus status,
                          @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        if(result.hasErrors()) {
            System.out.println("Validation Error");
        }
        //로그인한 유저의 정보
        User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());

        List<User> goWithList = new ArrayList<User>();
        if(form.getGowiths() != null) {
            for (int id : form.getGowiths()) goWithList.add(memberRepository.findUserById(id));
        }
        int weeklyId = weeklyService.register(form.getFile(),
                createWeekly(user, null, form.getTitle(), form.getNation(),
                new TravelDate(form.getSdate(),form.getEdate()), new CrudDate(new Date(),null),
                        status, form.getText(), goWithList), user
        );

        return weeklyId;
    }


    /**
     * 선택한 여행의 위클리를 조회
     * @param : weeklyId
     * @return :
     * */
    @GetMapping("/weekly/{weeklyId}")
    public String getWeekly(@PathVariable("weeklyId") int weeklyId, Model model,  @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Weekly weekly = weeklyService.findWeekly(weeklyId);
        long period = ((weekly.getTravelDate().getEDate().getTime() - weekly.getTravelDate().getSDate().getTime()) / 1000)/ (24*60*60)+1;
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

        //로그인한 유저의 정보
        User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());

        model.addAttribute("user", user);
        //model.addAttribute("followerList", followerList);
        model.addAttribute("allWdList", allWdList);
        model.addAttribute("weekly",weekly);
        return "test/weekly";
    }
    @PostMapping("/weekly/{weeklyId}/update")
    @ResponseBody
    public int updateWeekly(@Valid WeeklyForm form, BindingResult result,
                             @RequestParam("status") OpenStatus status,
                             @PathVariable("weeklyId") int weeklyId) throws Exception {
        if(result.hasErrors()) {
            System.out.println("Validation Error");
        }
        weeklyService.updateWeekly(weeklyId, form, status);

        return weeklyId;
    }

    @PostMapping("/weekly/saveKeyword")
    @ResponseBody
    public String saveKeywords(@RequestBody KwordDTO dto) throws Exception {
        return weeklyService.saveKeyword(dto);
    }




}
