package tc.travelCarrier.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Gowith {

    @Id @GeneratedValue()
    @Column(name = "GOWITH_NO")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "WEEKLY_ID")
    private Weekly weekly;

    private Long userId; //탈퇴하면 동행인 안뜨는거 방지하기위해 연관관계X


}
