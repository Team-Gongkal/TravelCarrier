package tc.travelCarrier.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Nation {

    @Id
    @Column(name="NATION_ID")
    private int id;

    @Column(name="NATION_NAME")
    private String name;

}
