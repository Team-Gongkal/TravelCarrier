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

/*    public TravelDate(Date sDate, Date eDate){
        this.sDate = sDate;
        this.eDate = eDate;
    }*/
    public TravelDate(String sDate, String eDate){
        this.sDate = sDate;
        this.eDate = eDate;
    }

    @Column(name="SDATE")
    private String sDate;
    @Column(name="EDATE")
    private String eDate;


}