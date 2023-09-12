package tc.travelCarrier.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Embeddable
@Getter
public class TravelDate {

    public TravelDate(){}
    public TravelDate(String sDate, String eDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        this.sDate = formatter.parse(sDate);
        this.eDate = formatter.parse(eDate);
    }

    @Temporal(TemporalType.DATE)
    @Column(name="SDATE")
    private Date sDate;

    @Temporal(TemporalType.DATE)
    @Column(name="EDATE")
    private Date eDate;


}