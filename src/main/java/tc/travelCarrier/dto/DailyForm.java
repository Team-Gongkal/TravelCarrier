package tc.travelCarrier.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter @ToString(exclude = "file") @AllArgsConstructor
public class DailyForm {
    public MultipartFile file;
    public String title;
    public Integer thumb;
    public String text;
    public String day;
    public Integer sort;
}
