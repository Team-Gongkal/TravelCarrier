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
        this.title = title;
        this.thumb = thumb;
        this.text = text;
        this.sort = sort;
        this.attachNo = attachNo;
        this.dupdate = dupdate;
    }
    /*public DailyForm(FileOrPath file, String title, Integer thumb,
                     String text, Integer sort, Integer attachNo) {
        //파일이면
        if(file.getFile() != null && file.getPath() == null){
            this.file = file.getFile();
            this.title = title;
            this.thumb = thumb;
            this.text = text;
            this.sort = sort;
            this.attachNo = attachNo;
        }else if(file.getFile() == null && file.getPath() != null){ //경로면
            this.file = null;
            this.title = title;
            this.thumb = thumb;
            this.text = text;
            this.sort = sort;
            this.attachNo = attachNo;
        }

    }*/
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
