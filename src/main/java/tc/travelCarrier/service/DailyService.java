package tc.travelCarrier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tc.travelCarrier.domain.AttachDaily;
import tc.travelCarrier.domain.Daily;
import tc.travelCarrier.domain.Weekly;
import tc.travelCarrier.dto.DailyDTO;
import tc.travelCarrier.dto.DailyForm;
import tc.travelCarrier.repository.DailyRepository;
import tc.travelCarrier.repository.WeeklyRepository;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class DailyService {

    private final DailyRepository dailyRepository;
    private final WeeklyRepository weeklyRepository;
    private final AttachService attachService;

    /**
     * 해당 위클리의 데일리정보 가져오는 메소드
     * */
    public List<DailyDTO> getAttachDaily(Weekly weekly){
        return dailyRepository.getAttachDaily(weekly);
    }

    // 첨부파일 데이터 수정
    public void updateAttachDaily(Map<String, List<DailyForm>> map) throws Exception {
        for(List<DailyForm> list : map.values()) {
            for (DailyForm form : list) {
                AttachDaily at = dailyRepository.findAttachDaily(form.getAttachNo());
                at.updateField(form);
                // 파일자체도 변경되었다면 변경해주기
                if(form.getDupdate().equals("change")){
                    // 이전 사진은 삭제
                    attachService.deleteServerFile(at.getFullThumbPath());
                    // 서버에 저장, saveArr = {newTitle,thumbPath};
                    String[] saveArr = attachService.saveAttach(form.getFile(),"daily");
                    at.updateFile(saveArr[0],saveArr[1]);
                }
            }
        }
    }

}
