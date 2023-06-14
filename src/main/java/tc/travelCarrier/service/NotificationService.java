package tc.travelCarrier.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import tc.travelCarrier.domain.*;
import tc.travelCarrier.repository.MemberRepository;
import tc.travelCarrier.repository.NotificationRepository;
import tc.travelCarrier.repository.WeeklyRepository;
import tc.travelCarrier.web.NotificationController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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

    public List<Notification> findNotificationByUserIdAndRead() {
        User suser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User activeUser = memberRepository.findUserById(suser.getId());
        // 읽음처리
        List<Notification> list = notificationRepository.findByReceiver(activeUser);
        for(Notification notification : list) notification.setIsRead(true);
        // 알림목록 리턴
        return list;
    }

    // 알림 삭제
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    //댓글작성알림 저장
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
            Notification notification = Notification.builder().sender(sender).receiver(receiver).notificationType(type).cdate(new Date()).title(reply.getAttachDaily().getDaily().getWeekly().getTitle()).url(url).isRead(false).build();
            notificationRepository.save(notification);
            sendEmitter(reply, receiver);
        }
    }

    // 위클리 태그 알림 저장
    public void saveTagNotification(Weekly weekly) {
        User activeUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User sender = memberRepository.findUserById(activeUser.getId());

        for(Gowith go : weekly.getGowiths()) {
            Notification notification = Notification.builder().
                    sender(sender)
                    .receiver(go.getUser())
                    .notificationType("gowith")
                    .cdate(new Date())
                    .title(weekly.getTitle())
                    .url("/weekly/"+weekly.getId())
                    .isRead(false)
                    .build();
            notificationRepository.save(notification);
            sendEmitter(weekly, go.getUser());
        }

    }

    // SSE로 실시간 댓글알림 전송
    private void sendEmitter(Reply reply, User receiver) {
        SseEmitter sseEmitter = NotificationController.sseEmitters.get(receiver.getId());
        try {
            if(reply.getOrigin() == null)  sseEmitter.send(SseEmitter.event().name("reply").data(reply.getId()));
            else sseEmitter.send(SseEmitter.event().name("re-reply").data(reply.getId()));
        } catch (Exception e) {
            NotificationController.sseEmitters.remove(receiver.getId());
        }
    }
    // SSE로 실시간 태그알림 전송
    private void sendEmitter(Weekly weekly, User receiver) {
/*        SseEmitter sseEmitter = NotificationController.sseEmitters.get(receiver.getId());
        try {
            if(reply.getOrigin() == null)  sseEmitter.send(SseEmitter.event().name("reply").data(reply.getId()));
            else sseEmitter.send(SseEmitter.event().name("re-reply").data(reply.getId()));
        } catch (Exception e) {
            NotificationController.sseEmitters.remove(receiver.getId());
        }*/
    }

}
