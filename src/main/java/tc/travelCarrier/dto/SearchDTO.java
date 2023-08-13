package tc.travelCarrier.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchDTO {

    private String type;
    private String detailType;
    private String travelerEmail;
    private int page;

    // 키워드 기반 검색
    private String keyword;
    // 날짜 기반 검색
    private String sdate;
    private String edate;
}
