package tc.travelCarrier.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import tc.travelCarrier.repository.AttachRepository;
import tc.travelCarrier.security.auth.PrincipalDetails;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.repository.MemberRepository;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
//@RequestMapping("/TravelCarrier")
public class MainController {

    private final MemberRepository memberRepository;
    private final AttachRepository attachRepository;

    @GetMapping("/")
    @Transactional
    public String mainPage( Model model,
                           @AuthenticationPrincipal PrincipalDetails principalDetails){
        User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());
        model.addAttribute("user", user);

        //각 나라별 랜덤이미지 로드(나라당 5장씩)
        Map<String, List<String>> randImages = attachRepository.getRandomAttachThumbsByNation(user.getId());
        for (String nationName : randImages.keySet()) {
            System.out.println("\n\n=======" + nationName + "=======");

            List<String> attachThumbs = randImages.get(nationName);

            for (String str : attachThumbs) {
                System.out.println(str);
            }
        }
        model.addAttribute("randImages",randImages);
        return "test/main";
    }

}
