package tc.travelCarrier.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Embeddable
@Getter
@Setter
public class TravelDate {

    private Date sDate;
    private Date eDate;
}