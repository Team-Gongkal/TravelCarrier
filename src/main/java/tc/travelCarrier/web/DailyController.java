package tc.travelCarrier.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tc.travelCarrier.domain.Attach;
import tc.travelCarrier.domain.AttachDaily;
import tc.travelCarrier.domain.Daily;
import tc.travelCarrier.domain.Weekly;
import tc.travelCarrier.service.DailyService;
import tc.travelCarrier.service.WeeklyService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DailyController {

    private final DailyService dailyService;
    private final WeeklyService weeklyService;

    /**
     * 데일리페이지 : 데일리정보 가져오기 + 위클리의 제목과날짜 세팅
     * 후자는 위클리페이지에서  tDate, title 셋팅해서 ajax로 넘겨줘야함..
     * */
    @GetMapping("/weekly/{weeklyId}/daily")
    public String getDaily(@PathVariable int weeklyId, Model model) {
        Weekly weekly = weeklyService.findWeekly(weeklyId); //weeklyId를 통해 weekly객체 찾음, 아마 영속성컨텍스트에 있으므로 쿼리 안나갈듯
        List<AttachDaily> attachDailies = new ArrayList<>();
        for(Daily daily : weekly.getDailys()) {
            attachDailies.add(dailyService.getAttachDaily(daily));
        }
        model.addAttribute("dailies", attachDailies);
        return "test2";
    }
}
