package tc.travelCarrier.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter @ToString(exclude = "file")
public class DailyForm {
    public DailyForm() {}

    public DailyForm(MultipartFile file, String title, Integer thumb,
                     String text, Integer sort, Integer attachNo, String dupdate) {
        this.file = file;

        if(!title.equals(" ")) this.title = title;
        else this.title = "";

        this.thumb = thumb;

        if(!text.equals(" ")) this.text = text;
        else this.text = "";

        this.sort = sort;
        this.attachNo = attachNo;
        this.dupdate = dupdate;
    }

    public MultipartFile file;
    public String title;
    public Integer thumb;
    public String text;
    public Integer sort;
    public Integer attachNo;
    public String dupdate;

    public String path;
    public String newFileName;


}
