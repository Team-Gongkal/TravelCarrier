package tc.travelCarrier.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import tc.travelCarrier.domain.TravelDate;
import tc.travelCarrier.domain.User;

import java.util.List;

@Getter @ToString
public class MyPageDTO {
    public MyPageDTO(){}
    @Builder
    public MyPageDTO(int  id, String title, TravelDate date, String thumbPath, List<String> goWithList) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.thumbPath = thumbPath;
        this.goWithList = goWithList;
    }
    //weeklyId, title, date, thumbPath, goWithList
    private int id;
    private String title;
    private TravelDate date;
    private String thumbPath;
    private List<String> goWithList; //user는 우선 프로필사진만!!
}
