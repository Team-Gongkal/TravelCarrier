package tc.travelCarrier.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter @ToString
public class KwordDTO {

    private int dailyId;
    private List<String> kwordList;

}
