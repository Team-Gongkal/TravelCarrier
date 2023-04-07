package tc.travelCarrier.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tc.travelCarrier.domain.*;
import tc.travelCarrier.dto.DailyDTO;
import tc.travelCarrier.dto.DailyForm;
import tc.travelCarrier.service.AttachService;
import tc.travelCarrier.service.DailyService;
import tc.travelCarrier.service.WeeklyService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/TravelCarrier")
@RequiredArgsConstructor
public class DailyController {

    private final DailyService dailyService;
    private final WeeklyService weeklyService;
    private final AttachService attachService;

    /**
     * 데일리페이지 : 데일리정보 가져오기 + 위클리의 제목과날짜 세팅
     * 후자는 위클리페이지에서  tDate, title 셋팅해서 ajax로 넘겨줘야함..
     * */
    @GetMapping("/weekly/{weeklyId}/daily")
    public String getDaily(@PathVariable("weeklyId") int weeklyId, Model model) {
        Weekly weekly = weeklyService.findWeekly(weeklyId);
        List<DailyDTO> dailies = dailyService.getAttachDaily(weekly);
        System.out.println(weekly.toString());
        Collections.sort(dailies);
        for(DailyDTO dto : dailies){
            System.out.println("dto : "+dto.toString());
        }

        //일수 계산
        long period = ((weekly.getTravelDate().getEDate().getTime()
                        - weekly.getTravelDate().getSDate().getTime()) / 1000)/ (24*60*60)+1;
        System.out.println( period + " 일 차이");
        model.addAttribute("period", period);
        model.addAttribute("dailies", dailies);
        model.addAttribute("weekly",weekly);
        return "test/daily(modal)";
    }

    /**
     * 데일리 폼작성 후 데일리정보 등록하는 메소드
     * @param :
     * @return :
     * */
    @PostMapping("/weekly/{weeklyId}/daily/create")
    @ResponseBody
    public String createDaily(@PathVariable("weeklyId") int weeklyId,
                              @ModelAttribute DailyForm formData) throws Exception {
        System.out.println(formData);
        System.out.println("===========================================");
        //1.첨부파일을 서버에 저장
        String[] saveArr = attachService.saveAttachDaily(formData.getFile());
        //2.AttachWeekly 엔티티 생성해서 DB에도 저장
        // daily -> attach 순으로 저장되어야함.. daily는 DAY의 종류만큼만 반복
        // find를 통해 이미 존재하는지 찾고, daliy를 리턴받아서 attach 저장하면 되겠다!!
        //Daily daily = dailyService.getDaily(weeklyId, formData.getDay());


        //dailyService.save(attachDaily);
        System.out.println("잘햇나 확인");

        return "success";
    }
}

