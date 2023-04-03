package tc.travelCarrier.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
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
        this.isThumb = isThumb;
    }
    private int attachNo;
    private String dailyDate;
    private int attachDailySort;
    private String attachDailyTitle;
    private String attachDailyText;
    private String attachThumb;
    private boolean isThumb;

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("DailyDTO [attachNo=").append(attachNo);
        str.append(", dailyDate=").append(dailyDate);
        str.append(", attachDailySort=").append(attachDailySort);
        str.append(", attachDailyTitle=").append(attachDailyTitle);
        str.append(", attachDailyText=").append(attachDailyText);
        str.append(", attachThumb=").append(attachThumb);
        str.append(", isThumb=").append(isThumb);
        return str.toString();
    }

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
