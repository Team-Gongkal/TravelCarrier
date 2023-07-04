package tc.travelCarrier.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tc.travelCarrier.domain.TravelDate;
import tc.travelCarrier.domain.User;

import java.util.List;

@Getter @ToString @Setter
public class MyPageDTO {
    public MyPageDTO(){}
    @Builder(builderMethodName = "weeklyBuilder")
    public MyPageDTO(int  id, String title, TravelDate date, String thumbPath, List<String> goWithList) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.thumbPath = thumbPath;
        this.goWithList = goWithList;
    }

    @Builder(builderMethodName = "followerBuilder")
    public MyPageDTO(int id, String name, String thumbPath, String backgroundThumbPath) {
        this.id = id;
        this.name = name;
        this.thumbPath = thumbPath;
        this.backgroundThumbPath = backgroundThumbPath;
    }
    //weeklyId, title, date, thumbPath, goWithList
    private int id;
    private String title;
    private TravelDate date;
    private String thumbPath;
    private List<String> goWithList; //user는 우선 프로필사진만!!

    private String name;
    private String backgroundThumbPath;

    public static MyPageDTO generateFollowerDTO(String name, int id, String thumbPath, String bgPath){
        MyPageDTO dto = new MyPageDTO();
        dto.setName(name);
        dto.setId(id);
        dto.setThumbPath(thumbPath);
        dto.setBackgroundThumbPath(bgPath);

        return dto;
    }

}
