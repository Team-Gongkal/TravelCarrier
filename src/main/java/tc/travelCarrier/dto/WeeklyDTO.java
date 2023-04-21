package tc.travelCarrier.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tc.travelCarrier.domain.Kword;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class WeeklyDTO {
    public WeeklyDTO(){}
    public WeeklyDTO(int dayNum, Date realDate){
        this.dailyDate = "DAY"+dayNum;
        this.realDate = new SimpleDateFormat("MM.dd").format(realDate);
    }

    public WeeklyDTO(int dailyId, String dailyDate, String thumbPath, Date sDate) {
        this.dailyId = dailyId;
        this.dailyDate = dailyDate;
        this.thumbPath = thumbPath.substring(thumbPath.indexOf("/static") + 7);

        int dayNumber = Integer.parseInt(dailyDate.substring(3)); // daily_date에서 DAY 다음의 숫자 추출
        Calendar cal = Calendar.getInstance();
        cal.setTime(sDate);
        cal.add(Calendar.DATE, dayNumber - 1); // DAY1은 1일 후, DAY2는 2일 후... 날짜 계산
        Date resultDate = cal.getTime();
        this.realDate = new SimpleDateFormat("MM.dd").format(resultDate);

        this.keywords = new ArrayList<>();
        System.out.println("================================");
        System.out.println("================================");
        System.out.println(this.dailyId+", "+this.dailyDate+", "+this.thumbPath+", "+realDate+", ");
        //if(keywords.size() >0) System.out.println(keywords.get(0));
        System.out.println("================================");
    }


    private int dailyId;
    private String dailyDate;
    private String thumbPath;
    private String realDate;
    private List<String> keywords;

    public void addKeywords(String keyword) {
        keywords.add(keyword);
    }


}
