package tc.travelCarrier.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tc.travelCarrier.security.auth.PrincipalDetails;
import tc.travelCarrier.domain.*;
import tc.travelCarrier.dto.MyPageDTO;
import tc.travelCarrier.dto.SearchDTO;
import tc.travelCarrier.repository.FollowRepository;
import tc.travelCarrier.repository.MemberRepository;
import tc.travelCarrier.repository.WeeklyRepository;
import tc.travelCarrier.repository.WeeklySearchRepository;
import tc.travelCarrier.service.MemberService;
import tc.travelCarrier.service.SearchService;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static tc.travelCarrier.dto.MyPageDTO.generateFollowerDTO;

@Controller
@RequiredArgsConstructor
//@RequestMapping("/TravelCarrier")
public class MyPageController {
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final WeeklyRepository weeklyRepository;
    private final FollowRepository followRepository;
    private final WeeklySearchRepository weeklySearchRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final SearchService searchService;


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

        return "test/mypage";
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
                        f.getFDate(), f.getFollower().getEmail()));
                //System.out.println(f.getFollower().getName()+", "+dto.toString());
            }
        } else if(type.equals("follower")) {
            for (Follower f : fPage) {
                result.add(generateFollowerDTO(f.getUser().getName(), f.getUser().getId(), f.getUser().getAttachUser().getThumbPath(),
                        f.getUser().getAttachUserBackground() == null ? null : f.getUser().getAttachUserBackground().getThumbPath(),
                        f.getFDate(), f.getUser().getEmail()));
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

    // targetUser를 user가 팔로우
    @GetMapping("/member/following/{email}")
    public ResponseEntity follow(@PathVariable String email, @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {
        User targetUser = memberRepository.findUserByEmail(email);
        User loginUser = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());
        memberService.followMember(loginUser, targetUser);

        return ResponseEntity.ok(null);
    }
    // targetUser를 user가 언팔로우
    @GetMapping("/member/unfollow/{email}")
    public ResponseEntity unfollow(@PathVariable String email, @AuthenticationPrincipal PrincipalDetails principalDetails){
        User targetUser = memberRepository.findUserByEmail(email);
        User loginUser = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());
        Follower removeRow = followRepository.findByUserAndFollower(loginUser, targetUser);
        followRepository.delete(removeRow);
        return ResponseEntity.ok(null);
    }


    // 마이페이지
    @GetMapping("/member/{email}")
    public String travelerPage(@PathVariable String email, Model model,
                         @AuthenticationPrincipal PrincipalDetails principalDetails,
                         Pageable pageable){
        User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());
        User traveler = memberRepository.findUserByEmail(email);

        //만약 본인이라면 마이페이지로 리다이렉트
        if(user.getId() == traveler.getId()) return "redirect:/TravelCarrier/member/mypage";

        model.addAttribute("user",user);
        // 헤더에 들어가는거임!!
        model.addAttribute("profile",traveler);
        // 이메일 : profile.email, 프사 : profile.attachUser.thumbPath, 이름 : profile.name, 배경사진 : profile.attachUserBackground.thumbPath

        // 접속자가 트래블러를 팔로우하는지 확인
        boolean isFollowingTraveler = false;
        for(Follower f : user.getFollowers()){
            if(f.getFollower().getId() == traveler.getId()) isFollowingTraveler = true;
        }
        model.addAttribute("isFollowingTraveler", isFollowingTraveler);

        // 내가 팔로워의 팔로워목록에 있는지 확인
        boolean follow = false;
        for(Follower f : traveler.getFollowers()){
            if(f.getFollower().getId() == user.getId()) follow = true;
        }
        model.addAttribute("follow", follow);

        //다이어리 갯수 로드
        Page<Weekly> weeklyPage = searchService.findWeeklyPaging(traveler, pageable);
        List<MyPageDTO> diaryList = transferCheckWeeklyDTO(traveler, user, weeklyPage);
        model.addAttribute("diaryList",diaryList.size());

        //태그된 다이어리 갯수 로드
        Page<Weekly> tagWeeklyPage = searchService.findTagWeeklyPaging(traveler, pageable);
        List<MyPageDTO> tagDiaryList = transferCheckTagWeeklyDTO(traveler, user, tagWeeklyPage);
        model.addAttribute("tagDiaryList", tagDiaryList.size());
        
        //팔로워 로드
        model.addAttribute("followers",traveler.getFollowers().size());

        return "test/traveler";
    }

    //트래블러 페이지 페이징
    @PostMapping("/member/{email}/page")
    @ResponseBody
    public List<MyPageDTO> pagingTravelerPage(@PathVariable String email,
                                              @AuthenticationPrincipal PrincipalDetails principalDetails,
                                              @RequestBody SearchDTO searchDTO, Pageable pageable){
        System.out.println(searchDTO.toString());
        User traveler = memberRepository.findUserByEmail(email);
        User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());
        String type = searchDTO.getType();
        /*
            정리하자면, 현재 트래블러의 페이지를 방문했을때
            tag : ALL, FOLLOW(user가 글쓴이의 팔로잉목록에 있을경우만), ME(Gowith에 user가 있거나 글쓴이가 user일경우)
            dia : ALL, FOLLOW(user가 traveler의 팔로잉목록에 있을경우만), ME(Gowith에 user가 있을경우만)
         */


        if(type.equals("dia")){
            Page<Weekly> weeklyPage = searchService.findWeeklyPaging(traveler, pageable);
            return transferCheckWeeklyDTO(traveler, user, weeklyPage);
        }
        else if(type.equals("tag")){
            Page<Weekly> weeklyPage = searchService.findTagWeeklyPaging(traveler, pageable);
            return transferCheckTagWeeklyDTO(traveler, user, weeklyPage);
        }
        else if(type.equals("tra")){
            if(searchDTO.getDetailType().equals("following")) {
                Page<Follower> followingPage = memberRepository.getFollowingPaging(traveler, pageable);
                return transferFollowerDTO("following", followingPage);
            }
            else if(searchDTO.getDetailType().equals("follower")) {
                Page<Follower> followerPage = memberRepository.getFollowerPaging(traveler,pageable);
                return transferFollowerDTO("follower", followerPage);
            }

        }
        return null;
    }

    //해당 트래블러의 다이어리에 대해 접근할수있는 데이터만 남기기
    //dia : ALL, FOLLOW(user가 traveler의 팔로잉목록에 있을경우만), ME(Gowith에 user가 있을경우만)
    private List<MyPageDTO> transferCheckWeeklyDTO(User traveler, User user, Page<Weekly> weeklyPage) {
        // dto : weeklyId, title, date, thumbPath, goWithList
        List<MyPageDTO> result = new ArrayList<>();
        //내가 traveler의 팔로잉목록에 있는지 확인
        boolean isFollowUser = false;
        for(Follower f : traveler.getFollowers()){
            if(f.getFollower().getId() == user.getId()){
                isFollowUser = true;
                break;
            }
        }
        for(Weekly w : weeklyPage){
            if(w.getStatus() == OpenStatus.FOLLOW && !isFollowUser) continue;
            if(w.getStatus() == OpenStatus.ME){
                boolean flag = false;
                for(Gowith g : w.getGowiths()) {
                    if(g.getUser().getId() == user.getId()){
                        flag = true;
                    }
                }
                if(!flag) continue;
            }

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

    //tag : ALL, FOLLOW(user가 글쓴이의 팔로잉목록에 있을경우만), ME(Gowith에 user가 있거나 글쓴이가 user일경우)
    private List<MyPageDTO> transferCheckTagWeeklyDTO(User traveler, User user, Page<Weekly> weeklyPage) {
        // dto : weeklyId, title, date, thumbPath, goWithList
        List<MyPageDTO> result = new ArrayList<>();
        for(Weekly w : weeklyPage){
            // FOLLOWER : 나도 글쓴이의 팔로워이거나 글쓴이가 본인이라면 띄우기
            //즉 글쓴이가 test2이고, 로그인한사람이 test2이면 자기자신은 스스로의 팔로워가아니므로 뜨지않는 오류를 해결
            if(w.getStatus() == OpenStatus.FOLLOW){
                boolean isFollowUser = false;
                for(Follower f : w.getUser().getFollowers()){
                    if(f.getFollower().getId() == user.getId()){
                        isFollowUser = true;
                        break;
                    } else if(w.getUser().getId() == user.getId()){
                        isFollowUser = true;
                        break;
                    }
                }
                if(!isFollowUser) continue;
            }
            if(w.getStatus() == OpenStatus.ME){
                if(w.getUser().getId() != user.getId()){
                    boolean flag = false;
                    for(Gowith g : w.getGowiths()) {
                        if(g.getUser().getId() == user.getId()){
                            flag = true;
                        }
                    }
                    if(!flag) continue;
                }
            }
            //hide된 내용은 보이면 안됨
            List<String> users = new ArrayList<>();
            boolean hide = false;
            for(Gowith g : w.getGowiths()) {
                users.add(g.getUser().getAttachUser().getThumbPath());
                if(g.getUser().getId() == traveler.getId()) hide = g.getHide();
            }
            if(hide) continue;

            MyPageDTO dto = MyPageDTO.weeklyBuilder()
                    .id(w.getId()).title(w.getTitle()).date(w.getTravelDate())
                    .thumbPath(w.getAttachWeekly().getThumbPath())
                    .goWithList(users)
                    .hide(hide)
                    .build();

            result.add(dto);
        }
        for(MyPageDTO d : result){
            System.out.println(d);
        }
        return result;
    }


}
