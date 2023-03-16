package tc.travelCarrier.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Embeddable
@Getter @Setter
public class CrudDate {

    private String cdate;
    private String udate;
}