package tc.travelCarrier.web;


import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import tc.travelCarrier.domain.OpenStatus;
import tc.travelCarrier.domain.TravelDate;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter @Setter
public class WeeklyForm {

    @NotEmpty(message = "여행 국가 정보는 필수항목 입니다.")
    private String nation;

    private MultipartFile file;

    private String sdate;
    private String edate;

    private String title;
    private String text;
    private HashSet<Integer> gowiths;
    private String status;

}
