package tc.travelCarrier.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import tc.travelCarrier.domain.Notification;
import tc.travelCarrier.domain.Reply;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.repository.MemberRepository;
import tc.travelCarrier.repository.NotificationRepository;
import tc.travelCarrier.web.NotificationController;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    public SseEmitter subscribe(int userId) {
        // 현재 클라이언트를 위한 SseEmitter 생성
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        try {
            System.out.println("연결 시도");
            // 연결!!
            sseEmitter.send(SseEmitter.event().name("connect"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("연결 됨");
        // user의 pk값을 key값으로 해서 SseEmitter를 저장
        NotificationController.sseEmitters.put(userId, sseEmitter);

        sseEmitter.onCompletion(() -> NotificationController.sseEmitters.remove(userId));
        sseEmitter.onTimeout(() -> NotificationController.sseEmitters.remove(userId));
        sseEmitter.onError((e) -> NotificationController.sseEmitters.remove(userId));

        return sseEmitter;
    }

    public List<Notification> findNotificationByUserId() {
        User suser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User activeUser = memberRepository.findUserById(suser.getId());
        return notificationRepository.findByReceiver(activeUser);
    }
    public void saveReplyNotification(Reply reply) {
        //AttachDaily ad, String text, User user, CrudDate cd, Reply origin

        User activeUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User sender = memberRepository.findUserById(activeUser.getId());

        //(User sender, User receiver, String notificationType, String content, Boolean isRead)
        String type;
        User receiver;
        String url;
        if(reply.getOrigin() == null) {
            type = "comment";
            receiver = reply.getAttachDaily().getDaily().getWeekly().getUser();
            url = "/weekly/"+reply.getAttachDaily().getDaily().getWeekly().getId()+"/daily";
        } else {
            type = "recomment";
            receiver = reply.getOrigin().getUser();
            url = "/weekly/"+reply.getOrigin().getAttachDaily().getDaily().getWeekly().getId()+"/daily";
        }

        // 본인이 본인글에 작성할땐 알림X
        if(receiver.getId() != sender.getId()) {
            Notification notification = Notification.builder().sender(sender).receiver(receiver).notificationType(type).cdate(reply.getCrudDate().getCdate()).title(reply.getAttachDaily().getDaily().getWeekly().getTitle()).url(url).isRead(false).build();
            notificationRepository.save(notification);
            sendEmitter(reply, receiver);
        }
    }

    private void sendEmitter(Reply reply, User receiver) {
        SseEmitter sseEmitter = NotificationController.sseEmitters.get(receiver.getId());
        try {
            if(reply.getOrigin() == null)  sseEmitter.send(SseEmitter.event().name("reply").data(reply.getId()));
            else sseEmitter.send(SseEmitter.event().name("re-reply").data(reply.getId()));
        } catch (Exception e) {
            NotificationController.sseEmitters.remove(receiver.getId());
        }
    }
}
