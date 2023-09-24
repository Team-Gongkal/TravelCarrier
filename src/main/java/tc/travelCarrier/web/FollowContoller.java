package tc.travelCarrier.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tc.travelCarrier.domain.Follower;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.repository.FollowRepository;
import tc.travelCarrier.repository.MemberRepository;
import tc.travelCarrier.repository.WeeklyRepository;
import tc.travelCarrier.repository.WeeklySearchRepository;
import tc.travelCarrier.security.auth.PrincipalDetails;
import tc.travelCarrier.service.AttachService;
import tc.travelCarrier.service.MemberService;
import tc.travelCarrier.service.SearchService;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class FollowContoller {
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final MemberService memberService;

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
}
