package tc.travelCarrier.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tc.travelCarrier.domain.TravelDate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Getter @ToString @Setter
public class MyPageDTO {
    //weeklyId, title, date, thumbPath, goWithList
    private int id;
    private String title;
    private TravelDate date;
    private String thumbPath;
    private List<String> goWithList; //user는 우선 프로필사진만!!

    private String name;
    private String email;
    private String backgroundThumbPath;
    private String fDate;

    private boolean hide;

    public MyPageDTO(){}
    @Builder(builderMethodName = "weeklyBuilder")
    public MyPageDTO(int  id, String title, TravelDate date, String thumbPath, List<String> goWithList, boolean hide) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.thumbPath = thumbPath;
        this.goWithList = goWithList;
        this.hide = hide;
    }

    // 이거 name 왜안되는지 납득이 안됨
    @Builder(builderMethodName = "followerBuilder")
    public MyPageDTO(int id, String name, String thumbPath, String backgroundThumbPath, Date fDate) {
        this.id = id;
        this.name = name;
        this.thumbPath = thumbPath;
        this.backgroundThumbPath = backgroundThumbPath;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.fDate = dateFormat.format(fDate);

    }

    public static MyPageDTO generateFollowerDTO(String name, int id, String thumbPath, String bgPath, Date fDate, String email){
        MyPageDTO dto = new MyPageDTO();
        dto.setName(name);
        dto.setId(id);
        dto.setThumbPath(thumbPath);
        dto.setBackgroundThumbPath(bgPath);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dto.setFDate(dateFormat.format(fDate));
        dto.setEmail(email);
        return dto;
    }

}
