package tc.travelCarrier.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class PrincipalDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername");
        System.out.println("email : "+email);
        User user = memberRepository.findUserByEmail(email);
        System.out.println("user :"+user);
        if(user == null) {
            System.out.println("예외 던져");
            throw new UsernameNotFoundException("NotFound account");
        }

        return new PrincipalDetails(user);
    }

}
