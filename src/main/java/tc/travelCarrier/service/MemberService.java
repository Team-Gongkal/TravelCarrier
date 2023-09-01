package tc.travelCarrier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tc.travelCarrier.domain.Follower;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.dto.MemberInfoDTO;
import tc.travelCarrier.repository.FollowRepository;

import java.io.IOException;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final FollowRepository followRepository;
    private final NotificationService notificationService;

    public void updateMemberInfo(MemberInfoDTO dto, User user){
        user.setName(dto.getNickName());
    }

    //팔로우시 DB에 팔로잉정보 저장, 알림 전송
    public void followMember(User loginUser, User targetUser) throws IOException {
        followRepository.save(Follower.builder()
                .user(loginUser)
                .follower(targetUser)
                .fDate(new Date())
                .build());
        notificationService.saveFollowNotification(targetUser, loginUser);
    }

}
