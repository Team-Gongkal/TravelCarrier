package tc.travelCarrier.auth;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tc.travelCarrier.domain.Role;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.repository.MemberRepository;

import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    //DefaultOAuth2UserService는 OAuth2로그인 시 loadUserByUsername메서드로 로그인한 유저가 DB에 저장되어있는지를 찾는다.
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo = null;	//
        String provider = userRequest.getClientRegistration().getRegistrationId();    //google
        String providerId="", password="", email="";
        Role role = Role.ROLE_USER;
        String uuid = UUID.randomUUID().toString().substring(0, 6);
        if(provider.equals("google")){
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
            provider = userRequest.getClientRegistration().getRegistrationId();    //google
            providerId =  oAuth2UserInfo.getProviderId();
            password = bCryptPasswordEncoder.encode("패스워드"+uuid);  // 사용자가 입력한 적은 없지만 만들어준다
            email = oAuth2User.getAttribute("email");
        }
        else if(provider.equals("naver")){
            oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
            provider = userRequest.getClientRegistration().getRegistrationId();    //google
            providerId =  oAuth2UserInfo.getProviderId();
            password = bCryptPasswordEncoder.encode("패스워드"+uuid);  // 사용자가 입력한 적은 없지만 만들어준다
            email = provider+"_"+providerId; //네이버는 자체 email을 제공하지 않으므로 식별자로 대체함
        }

        User byUsername = memberRepository.findUserByEmail(email);
        //DB에 없는 사용자라면 회원가입처리
        if(byUsername == null){
            byUsername = User.oauth2Register()
                    .username(generateRandomName()).password(password).email(email).role(role)
                    .provider(provider).providerId(providerId)
                    .build();
            memberRepository.save(byUsername);
        }

        return new PrincipalDetails(byUsername, oAuth2UserInfo);
    }

    // 랜덤 이름 발생 메소드
    private enum Prefix {
        야생의, 행복한, 자유로운, 모험적인, 열정적인, 탐험하는, 도전적인, 여유로운, 감성적인, 유쾌한
    }
    public String generateRandomName() {
        Prefix[] prefixes = Prefix.values();
        Random random = new Random();
        Prefix randomPrefix = prefixes[random.nextInt(prefixes.length)];
        return randomPrefix.name() + " 여행자";
    }
}
