package tc.travelCarrier.domain;


import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
@Getter @Setter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {
    public User(){}
    public User(String email, String pw, String name){
        this.email = email;
        this.pw = pw;
        this.name = name;
    }

    @Builder
    public User(String email, AttachUser attachUser, String name){
        this.email = email;
        this.attachUser = attachUser;
        this.name = name;
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

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private AttachUser attachUser;

    @OneToMany(mappedBy = "user")
    private List<Weekly> weeklys = new ArrayList<>();

    @OneToMany(mappedBy = "user")//위클리폼을 위한 로딩
    private List<Follower> followers = new ArrayList<>();


    // 계정이 갖고있는 권한 목록은 리턴
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection <GrantedAuthority> collectors = new ArrayList<>();
        collectors.add(() -> {
            return "계정별 등록할 권한";
        });

        return collectors;
    }

    @Override
    public String getPassword() {
        return pw;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
