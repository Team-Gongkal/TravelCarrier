package tc.travelCarrier.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Embeddable
@Getter
public class TravelDate {

    public TravelDate(){}

    public TravelDate(Date eDate, Date sDate){
        this.sDate = sDate;
        this.eDate = eDate;
    }

    @Column(name="EDATE")
    private Date sDate;
    @Column(name="SDATE")
    private Date eDate;


}