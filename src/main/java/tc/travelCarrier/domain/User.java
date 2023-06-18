package tc.travelCarrier.domain;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter @Setter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    public User(){}
    public User(String email, String pw, String name){
        this.email = email;
        this.pw = pw;
        this.name = name;
    }
    @Builder
    public User(String email, String pw){
        this.email = email;
        this.pw = pw;
        this.name = email;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USER_ID")
    private int id;

    @Column(name="USER_EMAIL")
    private String email;

    @Column(name="USER_PW")
    private String pw;

    @Column(name="USER_NAME")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name="USER_ROLE")
    private Role role;

    @CreationTimestamp  //자동으로 만들어준다
    @Column(name="USER_TIME")
    private Timestamp createTime;

    @Column(name="USER_PROVIDER")
    private String provider;    // oauth2를 이용할 경우 어떤 플랫폼을 이용하는지
    @Column(name="USER_PROVIDERID")
    private String providerId;  // oauth2를 이용할 경우 아이디값

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private AttachUser attachUser;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private AttachUserBackground attachUserBackground;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Weekly> weeklys = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)//위클리폼을 위한 로딩
    private List<Follower> followers = new ArrayList<>();

    @Builder(builderClassName = "UserDetailRegister", builderMethodName = "userDetailRegister")
    public User(String username, String password, String email, Role role) {
        this.name = username;
        this.pw = password;
        this.email = email;
        this.role = role;
    }
    @Builder(builderClassName = "OAuth2Register", builderMethodName = "oauth2Register")
    public User(String username, String password, String email, Role role, String provider, String providerId) {
        this.name = username;
        this.pw = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }
}