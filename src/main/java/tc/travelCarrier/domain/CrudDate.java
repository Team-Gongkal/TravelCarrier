package tc.travelCarrier.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Embeddable
@Getter @Setter
public class CrudDate {
    @Column(name="CDATE")
    private Date cdate;
    @Column(name="UDATE")
    private Date udate;
}