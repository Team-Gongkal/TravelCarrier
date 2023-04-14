package tc.travelCarrier.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class DailyDTO implements Comparable<DailyDTO> {
    public DailyDTO() {}
    public DailyDTO(int attachNo, String dailyDate, int attachDailySort,
                    String attachDailyTitle, String attachDailyText, String attachThumb,
                    boolean isThumb) {
        this.attachNo = attachNo;
        this.dailyDate = dailyDate;
        this.attachDailySort = attachDailySort;;
        this.attachDailyTitle = attachDailyTitle;
        this.attachDailyText = attachDailyText;
        this.attachThumb = attachThumb.substring(81); //  ~/static 까지의 경로를 생략해준다.
        this.thumb = isThumb;
    }
    private int attachNo;
    private String dailyDate;
    private int attachDailySort;
    private String attachDailyTitle;
    private String attachDailyText;
    private String attachThumb;
    private boolean thumb;


    @Override
    public int compareTo(DailyDTO dto) {
        int result = this.dailyDate.substring(3)
                        .compareTo(dto.dailyDate.substring(3));
        if (result == 0) {
            result = Integer.compare(this.attachDailySort, dto.attachDailySort);
        }
        return result;
    }
}
