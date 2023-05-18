package tc.travelCarrier.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Embeddable
@Getter @Setter
public class CrudDate {
    public CrudDate(){}

    public CrudDate(Date cdate, Date udate){
        this.cdate = cdate;
        this.udate = udate;
    }
    public CrudDate(String cdate, String udate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        this.cdate = formatter.parse(cdate);
        if(udate !=null) this.udate = formatter.parse(udate);
    }

    @Column(name="CDATE")
    private Date cdate;
    @Column(name="UDATE")
    private Date udate;
    @Column(name="DDATE")
    private Date ddate;

    public void setDdateReply(String ddate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        this.ddate = formatter.parse(ddate);
    }
}