package tc.travelCarrier.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import tc.travelCarrier.security.auth.PrincipalDetails;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.dto.MemberInfoDTO;
import tc.travelCarrier.repository.FollowRepository;
import tc.travelCarrier.repository.MemberRepository;
import tc.travelCarrier.repository.WeeklyRepository;
import tc.travelCarrier.repository.WeeklySearchRepository;
import tc.travelCarrier.service.AttachService;
import tc.travelCarrier.service.MemberService;
import tc.travelCarrier.service.SearchService;

import java.util.Map;

import static tc.travelCarrier.domain.Role.ROLE_USER;

@Controller
@RequiredArgsConstructor
//@RequestMapping("/TravelCarrier")
public class MemberContoller {

    private final MemberRepository memberRepository;
    private final AttachService attachService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberService memberService;

    //로그인
    @GetMapping("/member/login")
    public String memberLogin(Model model,
                              @RequestParam(value = "error", required = false) String error,
                              @RequestParam(value="exception", required = false) String exception){
        model.addAttribute("error",error);
        model.addAttribute("exception",exception);

        return "test/login";
    }

    //아이디를 가지고 로그인
    @GetMapping("/member/sign")
    public String memberSignIn(Model model){

        model.addAttribute("email",null);
        return "test/sign_up";
    }
    
    // 회원가입창 이동
    @PostMapping("/member/sign")
    public String memberSignWithEmail(@RequestParam String email, Model model){
        System.out.println("메일스:"+email);
        model.addAttribute("email",email);
        return "test/sign_up";
    }

    // 회원가입시 아이디 중복확인
    @PostMapping("/member/sign/validCheck")
    public ResponseEntity<Boolean> memberEmaillCheck(@RequestBody Map<String, String> requestMap){
        if(memberRepository.findUserByEmail(requestMap.get("email"))!=null) return ResponseEntity.ok(false);
        return ResponseEntity.ok(true);
    }
    
    //회원가입 (회원정보저장)
    @PostMapping("/member/sign/create")
    public ResponseEntity memberSignIn(@RequestParam String email, @RequestParam String password, @RequestParam String name){
        User user = User.userDetailRegister().username(name).password(passwordEncoder.encode(password)).email(email).role(ROLE_USER).build();
        memberService.signIn(user);
        return ResponseEntity.ok(null);
    }

    // 실시간 SSE 통신을 위해 로그인 여부를 체크하는 메소드
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

