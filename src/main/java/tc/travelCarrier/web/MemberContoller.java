package tc.travelCarrier.web;

import static tc.travelCarrier.dto.MyPageDTO.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import tc.travelCarrier.auth.PrincipalDetails;
import tc.travelCarrier.domain.*;
import tc.travelCarrier.dto.MyPageDTO;
import tc.travelCarrier.dto.SearchDTO;
import tc.travelCarrier.repository.MemberRepository;
import tc.travelCarrier.repository.WeeklyRepository;
import tc.travelCarrier.repository.WeeklySearchRepository;
import tc.travelCarrier.service.SearchService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/TravelCarrier")
public class MemberContoller {

    private final MemberRepository memberRepository;
    private final WeeklyRepository weeklyRepository;
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

    // 마이페이지
    @GetMapping("/member/mypage")
    public String myPage(Model model,  @AuthenticationPrincipal PrincipalDetails principalDetails, Pageable pageable){
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
        // (반복문 필요 : 각 요소를 diary로) 썸네일 : diary.attachWeekly.thumbPath,
        // 제목 : diary.title, 기간 : diary.travelDate.sDate ~ diary.travelDate.eDate, 링크 : "TravelCarrier/weekly/"+diary.id
        // 태그된 친구 목록: diary.gowiths (참고로 이건 List형태로 들어있으므로 반복문 필요 : 각 요소를 gowith으로)
        // => gowith.user (프사는 gowith.user.attachUser.thumbPath, 이름은 gowith.user,name)

        model.addAttribute("followers",memberRepository.getFollowerPaging(user,pageable));
        model.addAttribute("followings",memberRepository.getFollowingPaging(user,pageable));
        // 반복문 필요 : 각 요소를 follower로
        // 친구이름 : follower.follower.name, 친구프사 :  follower.follower.attachUser.thumbPath,
        // 친구배사 :  follower.follower.attachUserBackground.thumbPath, 친구 프로필 링크 : "TravelCarrier/member/"+follower.follower.email,

        // 백그라운드 없으면 오류 날 수 있음 - 타임리프로 null체크해서 해결

        return "/test/mypage";
    }

    // 테스트페이지
    @GetMapping("/member/mypage/edit")
    public String editProfile(Model model,  @AuthenticationPrincipal PrincipalDetails principalDetails){
        User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());

        model.addAttribute("profile",user);
        model.addAttribute("user",user);
        model.addAttribute("followers",user.getFollowers());
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
            return transferWeeklyDTO(user, weeklyPage);
        }
        else if(type.equals("tag")){
            Page<Weekly> weeklyPage = searchService.findTagWeeklyPaging(user, pageable);
            return transferWeeklyDTO(user, weeklyPage);
        }
        else if(type.equals("tra")){
            if(searchDTO.getDetailType().equals("following")) {
                Page<Follower> followingPage = memberRepository.getFollowingPaging(user, pageable);
                return transferFollowerDTO("following", followingPage);
            }
            else if(searchDTO.getDetailType().equals("follower")) {
                Page<Follower> followerPage = memberRepository.getFollowerPaging(user,pageable);
                return transferFollowerDTO("follower", followerPage);
            }

        }
        return null;
    }

    private List<MyPageDTO> transferWeeklyDTO(User user, Page<Weekly> weeklyPage) {
        // dto : weeklyId, title, date, thumbPath, goWithList
        List<MyPageDTO> result = new ArrayList<>();
        for(Weekly w : weeklyPage){
            List<String> users = new ArrayList<>();
            boolean hide = false;
            for(Gowith g : w.getGowiths()) {
                users.add(g.getUser().getAttachUser().getThumbPath());
                if(g.getUser().getId() == user.getId()) hide = g.getHide();
            }

            MyPageDTO dto = MyPageDTO.weeklyBuilder()
                    .id(w.getId()).title(w.getTitle()).date(w.getTravelDate())
                    .thumbPath(w.getAttachWeekly().getThumbPath())
                    .goWithList(users)
                    .hide(hide)
                    .build();

            result.add(dto);
        }

        return result;
    }
    private List<MyPageDTO> transferFollowerDTO(String type, Page<Follower> fPage) {
        // dto : weeklyId, title, date, thumbPath, goWithList
        List<MyPageDTO> result = new ArrayList<>();
        if(type.equals("following")) {
            for (Follower f : fPage) {
                result.add(generateFollowerDTO(f.getFollower().getName(), f.getFollower().getId(), f.getFollower().getAttachUser().getThumbPath(),
                        f.getFollower().getAttachUserBackground() == null ? null : f.getFollower().getAttachUserBackground().getThumbPath(),
                        f.getFDate()));
                //System.out.println(f.getFollower().getName()+", "+dto.toString());
            }
        } else if(type.equals("follower")) {
            for (Follower f : fPage) {
                result.add(generateFollowerDTO(f.getUser().getName(), f.getUser().getId(), f.getUser().getAttachUser().getThumbPath(),
                        f.getUser().getAttachUserBackground() == null ? null : f.getUser().getAttachUserBackground().getThumbPath(),
                        f.getFDate()));
                //System.out.println(f.getFollower().getName()+", "+dto.toString());
            }
        }
        return result;
    }


    //마이페이지 위클리 검색 (태그, 내글을 타입으로 구분)
    @PostMapping("/mypage/search")
    @ResponseBody
    public List<MyPageDTO> searchWeeklyByKeyword(@AuthenticationPrincipal PrincipalDetails principalDetails,
                             @RequestBody SearchDTO searchDTO, Pageable pageable){
        System.out.println(searchDTO.toString());
        User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());

        String type = searchDTO.getType();
        Page<Weekly> weeklyPage = null;
        if(type.equals("dia")) weeklyPage = searchService.findByTitleOrNationNameOrUserNameOrUserEmailContainingForCurrentUser(searchDTO.getKeyword(), user, pageable);
        else if(type.equals("tag")) weeklyPage = searchService.findTaggedWeekliesByKeywordAndUser(searchDTO.getKeyword(), user, pageable);
        else if(type.equals("tra")) {
            Page<Follower> follwerPage= searchService.findFollowerByNameAndEmail(searchDTO.getKeyword(), user, pageable);
            return transferFollowerDTO("following",follwerPage);
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



    //마이페이지 위클리 기간 검색
    @PostMapping("/mypage/search/date")
    @ResponseBody
    public List<MyPageDTO> searchWeeklyByDate(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                        @RequestBody SearchDTO searchDTO, Pageable pageable) throws ParseException {
        System.out.println(searchDTO.toString());
        User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());

        String type = searchDTO.getType();
        Page<Weekly> weeklyPage = null;
        if(type.equals("dia")) weeklyPage = searchService.findWeeklyPagingByDate(searchDTO, user, pageable);
        else if(type.equals("tag")) weeklyPage = searchService.findTagWeeklyPagingByDate(searchDTO, user, pageable);

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

    // 마이페이지
    @GetMapping("/member/{email}")
    public String myPage(@PathVariable String email, Model model,
                         @AuthenticationPrincipal PrincipalDetails principalDetails,
                         Pageable pageable){
        User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());
        User traveler = memberRepository.findUserByEmail(email);

        //만약 본인이라면
        if(user.getId() == traveler.getId()) return "redirect:/TravelCarrier/member/mypage";

        model.addAttribute("user",user);
        // 헤더에 들어가는거임!!
        model.addAttribute("profile",traveler);
        // 이메일 : profile.email, 프사 : profile.attachUser.thumbPath, 이름 : profile.name, 배경사진 : profile.attachUserBackground.thumbPath


        // 팔로워인지 확인
        boolean follow = false;
        for(Follower f : user.getFollowers()){
            if(f.getFollower().getId() == traveler.getId()) follow = true;
        }
        model.addAttribute("follow", follow);
        if(follow) model.addAttribute("diaryList",weeklySearchRepository.findFollowWeekliesByTraveler(traveler, OpenStatus.ME,pageable));
        else model.addAttribute("diaryList",weeklySearchRepository.findNotFollowWeekliesByTraveler(traveler,OpenStatus.ALL,pageable));

        List<Weekly> tagDiaryList = new ArrayList<>();
        for(Weekly w : weeklyRepository.getTagWeeklys(traveler)){
            // ME : 글쓴이 본인이거나 태그된사람이면 띄우기
            if(w.getStatus() == OpenStatus.ME){
                if(w.getUser().getId() == user.getId()) tagDiaryList.add(w);
                else {
                    for(Gowith g : w.getGowiths()){
                        if(g.getUser().getId() == user.getId()) tagDiaryList.add(w);
                        break;
                    }
                }
            }
            // FOLLOWER : 나도 글쓴이의 팔로워라면 띄우기
            if(w.getStatus() == OpenStatus.FOLLOW){
                for(Follower f : w.getUser().getFollowers()){
                    if(f.getUser().getId() == user.getId()) tagDiaryList.add(w);
                    break;
                }
            }
            // ALL : 무조건 띄우기
            if(w.getStatus() == OpenStatus.ALL) tagDiaryList.add(w);
        }
        model.addAttribute("tagDiaryList", tagDiaryList);

        model.addAttribute("followers",traveler.getFollowers());


        return "/test/traveler";
    }

}

