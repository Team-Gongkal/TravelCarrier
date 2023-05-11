package tc.travelCarrier.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import tc.travelCarrier.domain.OpenStatus;
import tc.travelCarrier.domain.TravelDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter @Setter @ToString
public class WeeklyForm {

    @NotEmpty(message = "여행 국가 정보는 필수항목 입니다.")
    private String nation;

    private MultipartFile file;

    @NotEmpty
    private String sdate;
    @NotEmpty
    private String edate;

    @NotNull
    private String title;
    @NotNull
    private String text;
    //private HashSet<Integer> gowiths;

    private Integer[] gowiths;

    @NotNull
    private OpenStatus status;

    //수정할때 필요한 필드
    private String thumbStatus;

}
