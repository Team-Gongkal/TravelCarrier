package tc.travelCarrier.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tc.travelCarrier.security.auth.PrincipalDetails;
import tc.travelCarrier.domain.*;
import tc.travelCarrier.dto.MyPageDTO;
import tc.travelCarrier.dto.SearchDTO;
import tc.travelCarrier.repository.MemberRepository;
import tc.travelCarrier.repository.WeeklyRepository;
import tc.travelCarrier.service.SearchService;
import java.text.ParseException;
import java.util.List;


@Controller
@RequiredArgsConstructor
//@RequestMapping("/TravelCarrier")
public class MyPageController {
    private final MemberRepository memberRepository;
    private final WeeklyRepository weeklyRepository;
    private final SearchService searchService;


    // 마이페이지 상세내용 빼고 셋팅
    @GetMapping("/member/mypage")
    public String myPage(Model model,  @AuthenticationPrincipal PrincipalDetails principalDetails, Pageable pageable){

        User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());
        model.addAttribute("profile",user);
        model.addAttribute("user",user);
        model.addAttribute("diaryList",searchService.findWeeklyPaging(user, pageable));
        model.addAttribute("tagDiaryList",searchService.findTagWeeklyPaging(user, pageable));
        model.addAttribute("followers",memberRepository.getFollowerPaging(user,pageable));
        model.addAttribute("followings",memberRepository.getFollowingPaging(user,pageable));

        return "test/mypage";
    }


    //마이페이지 페이징하여 내용셋팅
    @PostMapping("/mypage/page")
    @ResponseBody
    public List<MyPageDTO> pagingMyPage(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                        @RequestBody SearchDTO searchDTO, Pageable pageable){
        System.out.println(searchDTO.toString());
        User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());

        String type = searchDTO.getType();
        if(type.equals("dia")) return searchService.findWeeklyPaging(user, pageable);
        else if(type.equals("tag")) return searchService.findTagWeeklyPaging(user, pageable);
        else if(type.equals("tra")){
            if(searchDTO.getDetailType().equals("following")) return searchService.getFollowingPaging(user, pageable);
            else if(searchDTO.getDetailType().equals("follower")) return searchService.getFollowerPaging(user,pageable);
        }
        return null;
    }

    
    //마이페이지 검색어로 검색 (태그, 내글을 타입으로 구분)
    @PostMapping("/mypage/search")
    @ResponseBody
    public List<MyPageDTO> searchWeeklyByKeyword(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                 @RequestBody SearchDTO searchDTO, Pageable pageable){
        System.out.println(searchDTO.toString());
        User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());

        String type = searchDTO.getType();

        if(type.equals("dia")) return searchService.findByTitleOrNationNameOrUserNameOrUserEmailContainingForCurrentUser(searchDTO.getKeyword(), user, pageable);
        else if(type.equals("tag")) return searchService.findTaggedWeekliesByKeywordAndUser(searchDTO.getKeyword(), user, pageable);
        else if(type.equals("tra")) {
            return searchService.findFollowerByNameAndEmail(searchDTO.getDetailType(), searchDTO.getKeyword(), user, pageable);
        }
        return null;
    }



    //마이페이지 다이어리 기간으로 검색
    @PostMapping("/mypage/search/date")
    @ResponseBody
    public List<MyPageDTO> searchWeeklyByDate(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                              @RequestBody SearchDTO searchDTO, Pageable pageable) throws ParseException {
        System.out.println(searchDTO.toString());
        User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());

        String type = searchDTO.getType();
        if(type.equals("dia")) return searchService.findWeeklyPagingByDate(searchDTO, user, pageable);
        else if(type.equals("tag")) return searchService.findTagWeeklyPagingByDate(searchDTO, user, pageable);
        else return null;
    }

    // 친구의 마이페이지, 즉 트래블러 페이지 조회
    @GetMapping("/member/{email}")
    public String travelerPage(@PathVariable String email, Model model,
                         @AuthenticationPrincipal PrincipalDetails principalDetails,
                         Pageable pageable){
        User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());
        User traveler = memberRepository.findUserByEmail(email);

        //만약 본인이라면 마이페이지로 리다이렉트
        if(user.getId() == traveler.getId()) return "redirect:/member/mypage";

        model.addAttribute("user",user);
        model.addAttribute("profile",traveler);

        // 접속자가 트래블러를 팔로우하는지 확인
        boolean isFollowingTraveler = false;
        for(Follower f : user.getFollowers()){
            if(f.getFollower().getId() == traveler.getId()) isFollowingTraveler = true;
        }
        model.addAttribute("isFollowingTraveler", isFollowingTraveler);

        // 내가 팔로워의 팔로워목록에 있는지 확인, 즉 이 페이지의 주인이 나를 팔로우 하는지 확인
        boolean follow = false;
        for(Follower f : traveler.getFollowers()){
            if(f.getFollower().getId() == user.getId()) follow = true;
        }
        model.addAttribute("follow", follow);
        List<MyPageDTO> diaryList = searchService.findWeeklyPaging(traveler, user, pageable);
        model.addAttribute("diaryList",diaryList.size());
        List<MyPageDTO> tagDiaryList = searchService.findTagWeeklyPaging(traveler, user, pageable);
        model.addAttribute("tagDiaryList", tagDiaryList.size());
        model.addAttribute("followers",traveler.getFollowers().size());

        return "test/traveler";
    }

    //트래블러 페이지 각 상세내용 페이징
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

        if(type.equals("dia")) return searchService.findWeeklyPaging(traveler, user, pageable);
        else if(type.equals("tag")) return searchService.findTagWeeklyPaging(traveler, user, pageable);
        else if(type.equals("tra")){
            if(searchDTO.getDetailType().equals("following"))  return searchService.getFollowingPaging(traveler, user, pageable);
            else if(searchDTO.getDetailType().equals("follower")) return searchService.getFollowerPaging(traveler,user, pageable);
        }
        return null;
    }



}
