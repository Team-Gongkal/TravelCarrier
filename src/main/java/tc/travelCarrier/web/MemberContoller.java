package tc.travelCarrier.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import tc.travelCarrier.auth.PrincipalDetails;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.dto.MemberInfoDTO;
import tc.travelCarrier.repository.FollowRepository;
import tc.travelCarrier.repository.MemberRepository;
import tc.travelCarrier.repository.WeeklyRepository;
import tc.travelCarrier.repository.WeeklySearchRepository;
import tc.travelCarrier.service.AttachService;
import tc.travelCarrier.service.MemberService;
import tc.travelCarrier.service.SearchService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/TravelCarrier")
public class MemberContoller {

    private final MemberRepository memberRepository;
    private final AttachService attachService;
    private final WeeklyRepository weeklyRepository;
    private final FollowRepository followRepository;
    private final WeeklySearchRepository weeklySearchRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final SearchService searchService;
    private final MemberService memberService;

    @GetMapping("/member/login")
    public String memberLogin(Model model,
                              @RequestParam(value = "error", required = false) String error,
                              @RequestParam(value="exception", required = false) String exception){
        model.addAttribute("error",error);
        model.addAttribute("exception",exception);

        return "test/login";
    }

    @GetMapping("/member/sign")
    public String memberSignIn(Model model){

        model.addAttribute("email",null);
        return "test/sign_up";
    }

    @PostMapping("/member/sign")
    public String memberSignIn(@RequestParam String email, @RequestParam String pw, @RequestParam String name){
        User user = new User(email,passwordEncoder.encode(pw),name);
        memberRepository.save(user);
        return "test/login";
    }

    @PostMapping("/member/signWithEmail")
    public String memberSignWithEmail(@RequestParam String email, Model model){
        System.out.println("메일스:"+email);
        model.addAttribute("email",email);
        return "test/sign_up";
    }

    // 로그인했는지 확인하기
    @GetMapping("/member/login/check")
    @ResponseBody
    public String checkLogin(@AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println("어노테이션 있음");
        if(principalDetails!=null) return "true";
        else return "false";
    }


    // 멤버 프로필사진 변경
    @PostMapping(value="/member/profile")
    @ResponseBody
    public ResponseEntity registProfile(@RequestParam("profileImg") MultipartFile profileFile,
                                         @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());
        attachService.saveAttachUser(profileFile, user);
        return ResponseEntity.ok(null);
    }


    // 멤버 배경사진 변경
    @PostMapping(value="/member/background")
    @ResponseBody
    public ResponseEntity background (@RequestParam("backgroundImg") MultipartFile backgroundImgFile,
                                      @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());
        attachService.saveAttachUserBackground(backgroundImgFile, user);
        return ResponseEntity.ok(null);
    }

    // 멤버 정보 변경
    @PostMapping(value="/member/info")
    public ResponseEntity updateInfo(@RequestBody MemberInfoDTO memberInfoDTO,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());
        memberService.updateMemberInfo(memberInfoDTO,user);
        return ResponseEntity.ok(null);
    }

}

