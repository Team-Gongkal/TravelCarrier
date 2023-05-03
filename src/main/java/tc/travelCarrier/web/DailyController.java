package tc.travelCarrier.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tc.travelCarrier.domain.*;
import tc.travelCarrier.dto.DailyDTO;
import tc.travelCarrier.dto.DailyDTOComparator;
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
        Collections.sort(dailies, new DailyDTOComparator());

        Map<String, List<DailyDTO>> groupedDailies = new LinkedHashMap<>();
        for (DailyDTO daily : dailies) {
            String dailyDate = daily.getDailyDate();
            List<DailyDTO> list = groupedDailies.getOrDefault(dailyDate, new ArrayList<>());
            list.add(daily);
            groupedDailies.put(dailyDate, list);
        }
        //일수 계산
        long period = ((weekly.getTravelDate().getEDate().getTime() - weekly.getTravelDate().getSDate().getTime()) / 1000)/ (24*60*60)+1;

        model.addAttribute("period", period);
        model.addAttribute("dailies", dailies);
        model.addAttribute("weekly",weekly);
        model.addAttribute("groupedDailies", groupedDailies);
        System.out.println("period"+period);
        return "test/daily(modal)";
    }

    /**
     * 데일리 폼작성 후 데일리정보 등록하는 메소드
     * @param :
     * @return :
     * */
    @PostMapping("/weekly/{weeklyId}/daily/create")
    @ResponseBody
    public List<DailyDTO> createDaily (@PathVariable("weeklyId") int weeklyId,
                               @RequestParam("files") List<MultipartFile> fileList,
                               @RequestParam("titles") List<String> titleList,
                               @RequestParam("texts") List<String> textList,
                               @RequestParam("days") List<String> dayList,
                               @RequestParam("sorts") List<Integer> sortList,
                               @RequestParam("thumbs") List<Integer> thumbList,
                               @RequestParam("attachNos") List<Integer> attachNoList,
                               @RequestParam("dupdate") List<String> dupdateList,
                               @RequestParam("deleteNos") List<Integer> deleteNos) throws Exception {
        Weekly weekly = weeklyService.findWeekly(weeklyId);
        System.out.println("타이틀 : "+weekly.getTitle());
        //map 형태로 데이터 바인딩
        Map<String, List<DailyForm>> dailyMap = new HashMap<>();
        for(int i=0; i<fileList.size(); i++){
            String day = dayList.get(i);
            List<DailyForm> dailyFormList = dailyMap.getOrDefault(day, new ArrayList<>());
            DailyForm dailyForm = new DailyForm(fileList.get(i),  titleList.get(i), thumbList.get(i),
                    textList.get(i),
                    sortList.get(i),
                    attachNoList.get(i),
                    dupdateList.get(i));
            dailyFormList.add(dailyForm);
            dailyMap.put(day, dailyFormList);
        }


        // 삭제로직 :deleteNos의 attachNo들을 삭제
        attachService.deleteAttachDaily(deleteNos);
        // 추가로직
        Map<String, List<DailyForm>> newFileMap = getNewFileMap(dailyMap);
        attachService.saveAttachDaily(weekly,newFileMap);
        // 수정로직
        dailyService.updateAttachDaily(dailyMap);

        // 응답
        List<DailyDTO> dailies = dailyService.getAttachDaily(weekly);
        Collections.sort(dailies, new DailyDTOComparator());

        System.out.println(dailies);

        return dailies;
    }

    private Map<String, List<DailyForm>> getNewFileMap(Map<String, List<DailyForm>> dailyMap) {
        Map<String, List<DailyForm>> newFileMap = new HashMap<>();
        for (Map.Entry<String, List<DailyForm>> entry : dailyMap.entrySet()) {
            List<DailyForm> dailyFormList = entry.getValue();
            List<DailyForm> filteredList = new ArrayList<>();
            // -1인 데이터를 찾아서 삭제
            Iterator<DailyForm> iterator = dailyFormList.iterator();
            while (iterator.hasNext()) {
                DailyForm dailyForm = iterator.next();
                if (dailyForm.getAttachNo() == -1) {
                    iterator.remove();
                    filteredList.add(dailyForm);
                }
            }
            // 새로운 맵에 추가
            newFileMap.put(entry.getKey(), filteredList);
        }
        return newFileMap;
    }

}

