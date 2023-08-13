package tc.travelCarrier.web;

import static tc.travelCarrier.domain.Weekly.createWeekly;
import static tc.travelCarrier.dto.MyPageDTO.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tc.travelCarrier.auth.PrincipalDetails;
import tc.travelCarrier.domain.*;
import tc.travelCarrier.dto.MyPageDTO;
import tc.travelCarrier.dto.SearchDTO;
import tc.travelCarrier.dto.WeeklyForm;
import tc.travelCarrier.repository.FollowRepository;
import tc.travelCarrier.repository.MemberRepository;
import tc.travelCarrier.repository.WeeklyRepository;
import tc.travelCarrier.repository.WeeklySearchRepository;
import tc.travelCarrier.service.MemberService;
import tc.travelCarrier.service.SearchService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/TravelCarrier")
public class MemberContoller {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final WeeklyRepository weeklyRepository;
    private final FollowRepository followRepository;
    private final WeeklySearchRepository weeklySearchRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final SearchService searchService;

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
    public String checkLogin(@AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println("어노테이션 있음");

        return "false";
    }


    // 멤버 프로필사진 변경
    @PostMapping(value="/member/profile")
    @ResponseBody
    public ResponseEntity regist(@RequestParam("profileImg") MultipartFile profileImg,
                                         @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        User user = principalDetails.getUser();
        memberService.register( profileImg, user);
        return ResponseEntity.ok(null);
    }


}

