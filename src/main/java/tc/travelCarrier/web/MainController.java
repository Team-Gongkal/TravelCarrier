package tc.travelCarrier.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import tc.travelCarrier.security.auth.PrincipalDetails;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.repository.MemberRepository;

@Controller
@RequiredArgsConstructor
//@RequestMapping("/TravelCarrier")
public class MainController {

    private final MemberRepository memberRepository;

    @GetMapping("/")
    @Transactional
    public String mainPage(Model model,  @AuthenticationPrincipal PrincipalDetails principalDetails){

        User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());
        model.addAttribute("user", user);

        return "test/main";
    }

}
