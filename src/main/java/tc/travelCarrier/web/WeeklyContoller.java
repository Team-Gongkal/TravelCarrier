package tc.travelCarrier.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import tc.travelCarrier.auth.PrincipalDetails;
import tc.travelCarrier.domain.*;
import tc.travelCarrier.dto.KwordDTO;
import tc.travelCarrier.dto.WeeklyDTO;
import tc.travelCarrier.dto.WeeklyForm;
import tc.travelCarrier.repository.MemberRepository;
import tc.travelCarrier.repository.NationRepository;
import tc.travelCarrier.repository.WeeklyRepository;
import tc.travelCarrier.service.WeeklyService;

import javax.validation.Valid;
import java.util.*;

import static tc.travelCarrier.domain.Weekly.createWeekly;
import static tc.travelCarrier.security.AuthChecker.checkWeeklyAuth;
import static tc.travelCarrier.security.AuthChecker.getReadAndUpdateAuth;

@Controller
@RequiredArgsConstructor
@RequestMapping("/TravelCarrier")
public class WeeklyContoller {

    private final WeeklyService weeklyService;
    private final MemberRepository memberRepository;
    private final NationRepository nationRepository;

    @GetMapping("/weeklyForm")
    public String getWeeklyForm(Model model,@AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        //로그인한 유저의 정보
        User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());
        List<Nation> nationList = nationRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("nationList", nationList);
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
        // nation 객체 찾기
        Nation nation = nationRepository.findNationById(form.getNation());

        System.out.println("디폴트 : "+form.getFile());
        List<User> goWithList = new ArrayList<User>();
        if(form.getGowiths() != null) {
            for (int id : form.getGowiths()) goWithList.add(memberRepository.findUserById(id));
        }
        int weeklyId = weeklyService.register(form.getFile(),
                createWeekly(user, null, form.getTitle(), nation,
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
        // if) 공개범위 : ALL : 전체공개, FOLLOW : 팔로워공개, ME : 나에게만
        Weekly weekly = weeklyService.findWeekly(weeklyId);
        List<WeeklyDTO> allWdList = getAllWeeklyData(weekly, weeklyId);

        User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());

        List<Nation> nationList = nationRepository.findAll();
        model.addAttribute("nationList", nationList);

        // 읽기,쓰기 권한 처리
        String[] answer = getReadAndUpdateAuth(weekly,user);
        model.addAttribute("readAuth", answer[0]);
        model.addAttribute("updateAuth", answer[1]);
        model.addAttribute("selfAuth", answer[2]);


        if(answer[0].equals("GRANTED") && answer[1].equals("DENIED")) {
            List<WeeklyDTO> wdList = weeklyService.findWeeklyDto(weeklyId);
            model.addAttribute("wdList",wdList);
        }
        model.addAttribute("user", user);
        model.addAttribute("allWdList", allWdList);
        model.addAttribute("weekly",weekly);
        return "test/weekly";
    }


    private List<WeeklyDTO> getAllWeeklyData(Weekly weekly, int weeklyId) {
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

        return allWdList;
    }

    // 위클리 수정 메소드
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

    // 위클리 삭제하기
    @DeleteMapping("/weekly/{weeklyId}")
    public ResponseEntity<Void> deleteWeekly(@PathVariable("weeklyId") int weeklyId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Weekly weekly = weeklyService.findWeekly(weeklyId);
        weeklyService.deleteWeekly(weekly,principalDetails.getUser());
        return ResponseEntity.ok().build();
    }

    //태그된 위클리목록중 숨기거나 보이게하는 메소드
    @PutMapping("/weekly/{weeklyId}")
    public ResponseEntity<Void> hideOrShowWeekly(@PathVariable("weeklyId") int weeklyId,
                                                 @RequestBody Map<String,String> request,
                                                 @AuthenticationPrincipal PrincipalDetails principalDetails) {
        try {
            Weekly weekly = weeklyService.findWeekly(weeklyId);
            User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());
            weeklyService.hideOrShowWeekly(weekly, request.get("type"), user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //500에러
        }
    }


}
