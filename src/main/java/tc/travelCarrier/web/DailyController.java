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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
     * @param : List<DailyForm>
     * @return : ajax로 해야되는데... getDaily()메소드로 받아오는거 그대로 받아와야됨
     * */
    @PostMapping("/weekly/daily/creates")
    public String createDailys(@RequestBody List<DailyForm> formList) throws Exception{

        //dailyForm : day, file, title, text, sort, isThumb
        System.out.println("===========================================");
        int weeklyId = 2;
        List<Kword> kwords = new ArrayList<>();
        for(DailyForm form : formList) {
            //1.첨부파일을 서버에 저장
            String[] saveArr = attachService.saveAttachDaily(form.getFile());
            System.out.println("!!!!!!!!!!!!!!서버저장 성공!!!!!!!!!!!!!!!");
            form.setDay("DAY1");
            form.setSort(1);
            form.setThumb(true);
            form.setTitle("데일리등록");
            form.setText("데일리지롱~~!!");
            //2.AttachWeekly 엔티티 생성해서 DB에도 저장
            AttachDaily attachDaily = AttachDaily.builder()
                    .attachTitle(saveArr[0])
                    .thumb(saveArr[1])
                    .daily(new Daily(weeklyService.findWeekly(weeklyId), form.getDay()))
                    .title(form.getTitle())
                    .text(form.getText())
                    .sort(form.getSort())
                    .isThumb(form.isThumb())
                    .build();
            dailyService.save(attachDaily);
        }
        System.out.println("잘햇나 확인");
        return "success";
    }

    @PostMapping("/weekly/daily/create")
    public void createDaily(@RequestPart("file") List<MultipartFile> files,
                            @RequestPart("title") List<String> titles,
                            @RequestPart("text") List<String> texts,
                            @RequestPart("thumb") List<Integer> thumbs,
                            @RequestParam("day") String day) {
        // 받은 데이터를 처리하는 코드
        System.out.println("길이!!!!!!!!!!!!!!!!!!!!!"+files.size());
    }
}

