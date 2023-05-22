package tc.travelCarrier.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.repository.MemberRepository;
import tc.travelCarrier.service.WeeklyService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/TravelCarrier")
public class MemberContoller {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @GetMapping("/member/login")
    public String memberLogin(Model model,
                              @RequestParam(value = "error", required = false) String error,
                              @RequestParam(value="exception", required = false) String exception){
        model.addAttribute("error",error);
        model.addAttribute("exception",exception);
        System.out.println("GET 접근 : "+error + ", "+exception);
        return "test/login";
    }

    @GetMapping("/member/sign")
    public String memberSignIn(){
        return "test/sign";
    }
    @PostMapping("/member/sign")
    public String memberSignIn(@RequestParam String email, @RequestParam String pw, @RequestParam String name){
        User user = new User(email,passwordEncoder.encode(pw),name);
        memberRepository.save(user);
        return "test/login";
    }

}
