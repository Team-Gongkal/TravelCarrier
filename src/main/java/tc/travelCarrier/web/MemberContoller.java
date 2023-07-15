package tc.travelCarrier.web;

import static tc.travelCarrier.dto.MyPageDTO.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import lombok.RequiredArgsConstructor;
import tc.travelCarrier.auth.PrincipalDetails;
import tc.travelCarrier.domain.Follower;
import tc.travelCarrier.domain.Gowith;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.domain.Weekly;
import tc.travelCarrier.dto.MyPageDTO;
import tc.travelCarrier.dto.SearchDTO;
import tc.travelCarrier.repository.MemberRepository;
import tc.travelCarrier.repository.WeeklyRepository;
import tc.travelCarrier.service.SearchService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/TravelCarrier")
public class MemberContoller {

    private final MemberRepository memberRepository;
    private final WeeklyRepository weeklyRepository;
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

    // 마이페이지
    @GetMapping("/member/mypage")
    public String myPage(Model model,  @AuthenticationPrincipal PrincipalDetails principalDetails){
/*      본인 프로필(이메일, 프로필사진, 이름, 배경사진) : profile
        내 다이어리(썸네일, 제목, 기간, 링크) : diaryList
        태그된 다이어리 (썸네일, 제목, 기간, 글작성자, 링크) : tagDiaryList
        친구목록 (친구이름, 친구프사, 친구배사, 친구프로필링크) : followers
        */
        User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());

        model.addAttribute("profile",user);
        model.addAttribute("user",user);
        // 이메일 : profile.email, 프사 : profile.attachUser.thumbPath, 이름 : profile.name, 배경사진 : profile.attachUserBackground.thumbPath

        model.addAttribute("diaryList",user.getWeeklys());
        // (반복문 필요 : 각 요소를 diary로) 썸네일 : diary.attachWeekly.thumbPath,
        // 제목 : diary.title, 기간 : diary.travelDate.sDate ~ diary.travelDate.eDate, 링크 : "TravelCarrier/weekly/"+diary.id
        // 태그된 친구 목록: diary.gowiths (참고로 이건 List형태로 들어있으므로 반복문 필요 : 각 요소를 gowith으로)
        // => gowith.user (프사는 gowith.user.attachUser.thumbPath, 이름은 gowith.user,name)

        model.addAttribute("tagDiaryList", weeklyRepository.getTagWeeklys(user));
        System.out.println("잠깐만요:");
        for(Weekly w : weeklyRepository.getTagWeeklys(user)){
            System.out.println("위클리 "+w.getTitle());
        }
        // (반복문 필요 : 각 요소를 diary로) 썸네일 : diary.attachWeekly.thumbPath,
        // 제목 : diary.title, 기간 : diary.travelDate.sDate ~ diary.travelDate.eDate, 링크 : "TravelCarrier/weekly/"+diary.id
        // 태그된 친구 목록: diary.gowiths (참고로 이건 List형태로 들어있으므로 반복문 필요 : 각 요소를 gowith으로)
        // => gowith.user (프사는 gowith.user.attachUser.thumbPath, 이름은 gowith.user,name)

        model.addAttribute("followers",user.getFollowers());
        // 반복문 필요 : 각 요소를 follower로
        // 친구이름 : follower.follower.name, 친구프사 :  follower.follower.attachUser.thumbPath,
        // 친구배사 :  follower.follower.attachUserBackground.thumbPath, 친구 프로필 링크 : "TravelCarrier/member/"+follower.follower.email,

        // 백그라운드 없으면 오류 날 수 있음 - 타임리프로 null체크해서 해결
/*
        System.out.println("profile "+activeUser.getEmail()+", "+activeUser.getAttachUser().getThumbPath()+", "+activeUser.getName()
        +", "+activeUser.getAttachUserBackground().getThumbPath());
        for(Weekly w : activeUser.getWeeklys()){
            System.out.println(w.getTitle()+", "+w.getTravelDate().getSDate()+", "+w.getTravelDate().getEDate()+", "
            +", TravelCarrier/weekly/"+w.getId());
        }
        for(Weekly w : weeklyRepository.getTagWeeklys(activeUser)){
            System.out.println(w.getTitle()+", "+w.getTravelDate().getSDate()+", "+w.getTravelDate().getEDate()+", "
                    +", TravelCarrier/weekly/"+w.getId());
        }
        for(Follower follower : activeUser.getFollowers()){
            System.out.println( follower.getFollower().getName()+", "+follower.getFollower().getAttachUser().getThumbPath()+", "+
                    // follower.getFollower().getAttachUserBackground().getThumbPath()+
                    ", TravelCarrier/member/"+follower.getFollower().getEmail());
        }
*/

        return "/test/edit_profile";
    }

    //마이페이지 페이징
    @PostMapping("/mypage/page")
    @ResponseBody
    public List<MyPageDTO> pagingMyPage(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                        @RequestBody SearchDTO searchDTO, Pageable pageable){
        System.out.println(searchDTO.toString());
        User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());

        String type = searchDTO.getType();
        if(type.equals("dia")){
            Page<Weekly> weeklyPage = searchService.findWeeklyPaging(user, pageable);
            return transferWeeklyDTO(weeklyPage);
        }
        else if(type.equals("tag")){
            Page<Weekly> weeklyPage = searchService.findTagWeeklyPaging(user, pageable);
            return transferWeeklyDTO(weeklyPage);
        }
        else if(type.equals("tra")){
            Page<Follower> followerPage = searchService.findMyFollower(user, pageable);
            return transferFollowerDTO(followerPage);
        }
        return null;
    }

    private List<MyPageDTO> transferWeeklyDTO(Page<Weekly> weeklyPage) {
        // dto : weeklyId, title, date, thumbPath, goWithList
        List<MyPageDTO> result = new ArrayList<>();
        for(Weekly w : weeklyPage){
            List<String> users = new ArrayList<>();
            for(Gowith g : w.getGowiths()) users.add(g.getUser().getAttachUser().getThumbPath());
            System.out.println("레소레소");
            System.out.println(w.getTitle());

            MyPageDTO dto = MyPageDTO.weeklyBuilder()
                    .id(w.getId()).title(w.getTitle()).date(w.getTravelDate())
                    .thumbPath(w.getAttachWeekly().getThumbPath())
                    .goWithList(users)
                    .build();

            result.add(dto);
        }

        return result;
    }
    private List<MyPageDTO> transferFollowerDTO(Page<Follower> followerPage) {
        // dto : weeklyId, title, date, thumbPath, goWithList
        List<MyPageDTO> result = new ArrayList<>();
        for(Follower f : followerPage){
/*            result.add(MyPageDTO.followerBuilder()
                    .id(f.getFollower().getId())
                    .name(f.getFollower().getName())
                    .thumbPath(f.getFollower().getAttachUser().getThumbPath())
                    .backgroundThumbPath(f.getFollower().getAttachUserBackground() == null ? null : f.getFollower().getAttachUserBackground().getThumbPath())
                    .fDate(f.getFDate())
                    .build());*/
            result.add(generateFollowerDTO(f.getFollower().getName(),f.getFollower().getId(),f.getFollower().getAttachUser().getThumbPath(),
                    f.getFollower().getAttachUserBackground() == null ? null : f.getFollower().getAttachUserBackground().getThumbPath(),
                    f.getFDate()));
            //System.out.println(f.getFollower().getName()+", "+dto.toString());
        }

        return result;
    }


    //마이페이지 위클리 검색 (태그, 내글을 타입으로 구분)
    @PostMapping("/mypage/search")
    @ResponseBody
    public List<MyPageDTO> searchWeekly(@AuthenticationPrincipal PrincipalDetails principalDetails,
                             @RequestBody SearchDTO searchDTO, Pageable pageable){
        System.out.println(searchDTO.toString());
        User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());

        String type = searchDTO.getType();
        Page<Weekly> weeklyPage = null;
        if(type.equals("dia")) weeklyPage = searchService.findByTitleOrNationNameOrUserNameOrUserEmailContainingForCurrentUser(searchDTO.getKeyword(), user, pageable);
        else if(type.equals("tag")) weeklyPage = searchService.findTaggedWeekliesByKeywordAndUser(searchDTO.getKeyword(), user, pageable);
        else if(type.equals("tra")) {
            Page<Follower> follwerPage= searchService.findFollowerByNameAndEmail(searchDTO.getKeyword(), user, pageable);
            return transferFollowerDTO(follwerPage);
        }

        //그대로 리턴하면 순환참조 오류 발생하므로 dto로 바꿔서 보내준다
        // dto : weeklyId, title, date, thumbPath, goWithList
        List<MyPageDTO> result = new ArrayList<>();
        for(Weekly w : weeklyPage){
            List<String> users = new ArrayList<>();
            for(Gowith g : w.getGowiths()) users.add(g.getUser().getAttachUser().getThumbPath());

            MyPageDTO dto = MyPageDTO.weeklyBuilder()
                    .id(w.getId()).title(w.getTitle()).date(w.getTravelDate())
                    .thumbPath(w.getAttachWeekly().getThumbPath()).goWithList(users)
                    .build();

            result.add(dto);
        }
        return result;
    }




}

