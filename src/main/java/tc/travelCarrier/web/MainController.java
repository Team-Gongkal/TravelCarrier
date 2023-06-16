package tc.travelCarrier.web;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;
import tc.travelCarrier.auth.PrincipalDetails;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.domain.Weekly;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tc.travelCarrier.repository.MemberRepository;

@Controller
@RequiredArgsConstructor
@RequestMapping("/TravelCarrier")
public class MainController {

    private final MemberRepository memberRepository;

    @GetMapping("/")
    @Transactional
    public String mainPage(Model model,  @AuthenticationPrincipal PrincipalDetails principalDetails){

        User user1 = principalDetails.getUser();
        System.out.println("user1 :" +user1.getEmail()+", "+user1.getName());
        model.addAttribute("user", user1);

        return "test/main";
    }

}
