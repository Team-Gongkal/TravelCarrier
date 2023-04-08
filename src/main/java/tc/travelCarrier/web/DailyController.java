package tc.travelCarrier.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tc.travelCarrier.domain.*;
import tc.travelCarrier.dto.DailyDTO;
import tc.travelCarrier.dto.DailyForm;
import tc.travelCarrier.service.AttachService;
import tc.travelCarrier.service.DailyService;
import tc.travelCarrier.service.WeeklyService;

import java.util.*;

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
    public String createDaily (@PathVariable("weeklyId") int weeklyId,
                               @RequestParam("files") List<MultipartFile> fileList,
                               @RequestParam("titles") List<String> titleList,
                               @RequestParam("texts") List<String> textList,
                               @RequestParam("days") List<String> dayList,
                               @RequestParam("sorts") List<Integer> sortList,
                               @RequestParam("thumbs") List<Integer> thumbList) throws Exception {
        System.out.println("===========================================");
        //map 형태로 데이터 받기
        //{DAY1, List<DailyForm>}
        Map<String, List<DailyForm>> dailyMap = new HashMap<>();
        for(int i=0; i<fileList.size(); i++){
            String day = dayList.get(i);
            List<DailyForm> dailyFormList = dailyMap.getOrDefault(day, new ArrayList<>());
            DailyForm dailyForm = new DailyForm( fileList.get(i),  titleList.get(i), thumbList.get(i),
                    textList.get(i), sortList.get(i) );
            dailyFormList.add(dailyForm);
            dailyMap.put(day, dailyFormList);
        }

        Weekly weekly = weeklyService.findWeekly(weeklyId);
        String result = attachService.saveAttachDaily(weekly,dailyMap);
        return "success";
    }
}

