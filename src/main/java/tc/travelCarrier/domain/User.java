package tc.travelCarrier.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class User {

    @Id @GeneratedValue
    @Column(name="USER_ID")
    private Long id;

    @Column(name="USER_EMAIL")
    private String email;

    @Column(name="USER_PW")
    private String pw;

    @Column(name="USER_NAME")
    private String name;

    @OneToMany(mappedBy = "user")
    private List<Weekly> weeklys = new ArrayList<>();

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private AttachUser attachUser;
}
