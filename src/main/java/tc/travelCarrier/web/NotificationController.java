package tc.travelCarrier.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import tc.travelCarrier.auth.PrincipalDetails;
import tc.travelCarrier.domain.Notification;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.dto.NotificationDTO;
import tc.travelCarrier.repository.MemberRepository;
import tc.travelCarrier.service.NotificationService;
import tc.travelCarrier.sse.SseService;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Controller
public class NotificationController {

    private final NotificationService notificationService;
    private final SseService sseService;

    public static Map<Integer, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    private final MemberRepository memberRepository;

    @GetMapping(value = "/TravelCarrier/sub", produces = "text/event-stream")
    public SseEmitter subscribe(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        System.out.println("액티브 : "+principalDetails.getUser().getId());
        int userId = principalDetails.getUser().getId();

        return sseService.subscribe(userId);
    }

    @GetMapping(value = "/TravelCarrier/notification")
    public ResponseEntity<List<NotificationDTO>> getNotification(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<Notification> list = notificationService.findNotificationByUserId(principalDetails.getUser());
        // sender와 receiver의 User 그자체(email,thumbPath,닉네임)
        // id와 isread와 content까지
        // 시간, 제목, url도 필요!!!
        List<NotificationDTO> dtoList = new ArrayList<NotificationDTO>();
        for(Notification nt : list){
            User sender = nt.getSender();
            dtoList.add(
                    NotificationDTO.builder()
                            .type(nt.getNotificationType())
                            .id(nt.getId())
                            .senderName(sender.getName())
                            .senderThumbPath(sender.getAttachUser().getThumbPath())
                            .isRead(nt.getIsRead())
                            .url(nt.getUrl())
                            .time(nt.getCdate())
                            .title(nt.getTitle() == null? null : nt.getTitle())
                            .build());
        }
        Collections.sort(dtoList); //최신순 정렬
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping(value = "/TravelCarrier/notification/{notificationId}")
    public ResponseEntity deleteNotification(@PathVariable("notificationId") Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok(null);
    }

    //읽음처리
    @GetMapping(value = "/TravelCarrier/notification/isRead")
    public ResponseEntity readNotification(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = memberRepository.findUserByEmail(principalDetails.getUser().getEmail());
        //user의 모든 알림을 읽음처리한다
        notificationService.readAll(user);
        return ResponseEntity.ok(null);
    }

    //안읽는 알림 있는지 찾기
    @GetMapping(value = "/TravelCarrier/notification/notRead")
    public ResponseEntity notReadNotification(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = memberRepository.findUserByEmail(principalDetails.getUser().getEmail());
        //user가 안읽은 알림이 있는지 확인
        return ResponseEntity.ok(notificationService.findNotificationByUserIdAndIsRead(user,false));
    }

}