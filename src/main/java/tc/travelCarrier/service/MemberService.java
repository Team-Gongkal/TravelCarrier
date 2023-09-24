package tc.travelCarrier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tc.travelCarrier.domain.*;
import tc.travelCarrier.dto.MemberInfoDTO;
import tc.travelCarrier.repository.AttachRepository;
import tc.travelCarrier.repository.FollowRepository;
import tc.travelCarrier.repository.MemberRepository;
import tc.travelCarrier.sse.SseService;

import java.io.IOException;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final FollowRepository followRepository;
    private final NotificationService notificationService;
    private final MemberRepository memberRepository;
    private final SseService sseService;
    private final AttachRepository attachRepository;
    @Value("${file.dir}")
    private String fileDir;
    @Value("${default.profile}")
    private String defaultProfile;
    @Value("${default.background}")
    private String defaultBackground;
    public void signIn(User user){
        User savedUser = memberRepository.save(user);
        attachRepository.saveProfilePic(AttachUser.builder().attachTitle(defaultProfile)
                .user(savedUser).thumb(fileDir+defaultProfile).build());
        attachRepository.saveBgPic(AttachUserBackground.builder().title(defaultBackground)
                .user(savedUser).path(fileDir+defaultBackground).build());
    }

    public void updateMemberInfo(MemberInfoDTO dto, User user){
        user.setName(dto.getNickName());
    }

    //팔로우시 DB에 팔로잉정보 저장, 알림 전송
    public void followMember(User loginUser, User targetUser) throws IOException {
        //이미 친구인지 확인
        Follower f = followRepository.findByUserAndFollower(loginUser, targetUser);
        if( f != null) return; //이미 친구이면 그대로 종료
        followRepository.save(Follower.builder()
                .user(loginUser)
                .follower(targetUser)
                .fDate(new Date())
                .build());
        Notification notification = notificationService.saveFollowNotification(targetUser, loginUser);

        //알림전송
        sseService.sendEmitter(notification, notification.getReceiver());
    }

}
