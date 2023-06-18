package tc.travelCarrier.web;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.domain.Weekly;
import tc.travelCarrier.repository.MemberRepository;

@Controller
@RequiredArgsConstructor
@RequestMapping("/TravelCarrier")
public class MainController {

    private final MemberRepository memberRepository;

    @GetMapping("/")
    public String mainPage(Model model){
        //로그인한 유저의 정보
        User activeUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = memberRepository.findUserById(activeUser.getId());
        model.addAttribute("user", user);

        System.out.println("사이즈 나오니? = "+user.getWeeklys().size());
        for(Weekly w : user.getWeeklys()){
            System.out.println("위클리 제먹 : "+w.getTitle()
            +", 경로 : "+w.getAttachWeekly().getThumbPath());
        }
        return "test/main";
    }
}
