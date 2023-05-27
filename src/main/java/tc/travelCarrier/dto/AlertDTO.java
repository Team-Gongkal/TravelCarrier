package tc.travelCarrier.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AlertDTO {
    private int type;
    private String content;
    private String senderId;
    private String receiverId;
}
