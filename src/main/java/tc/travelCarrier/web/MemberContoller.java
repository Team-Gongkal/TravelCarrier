package tc.travelCarrier.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.repository.MemberRepository;
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

    // 로그인했는지 확인하기
    @GetMapping("/member/login/check")
    @ResponseBody
    public String checkLogin(){
        String check;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().equals("anonymousUser")) check = "anonymousUser";
        else check = "loginUser";

        return check;
    }

        // 마이페이지
    @GetMapping("/member/mypage")
    public String myPage(){
        return "/test/mypage";
    }


}
