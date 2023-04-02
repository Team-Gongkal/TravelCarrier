package tc.travelCarrier.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class DailyForm {
    private String day;
    private MultipartFile file;
    private String title;
    private String text;
    private int sort;
    private boolean thumb;
}
