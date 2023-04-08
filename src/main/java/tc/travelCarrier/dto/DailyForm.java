package tc.travelCarrier.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter @ToString(exclude = "file")
public class DailyForm {
    public DailyForm() {}
    public DailyForm(MultipartFile file, String title, Integer thumb,
                     String text, Integer sort) {
        this.file = file;
        this.title = title;
        this.thumb = thumb;
        this.text = text;
        this.sort = sort;
    }
    public MultipartFile file;
    public String title;
    public Integer thumb;
    public String text;
    public Integer sort;

    public String path;
    public String newFileName;

}
