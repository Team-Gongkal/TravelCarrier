package tc.travelCarrier.service;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
    
    // SSeEmitter 객체 생성
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

        sseEmitter.onCompletion(() -> NotificationController.sseEmitters.remove(sseEmitter));
        sseEmitter.onTimeout(() -> NotificationController.sseEmitters.remove(sseEmitter));
        sseEmitter.onError((e) -> NotificationController.sseEmitters.remove(sseEmitter));

        return sseEmitter;
    }

    //전체 알림 조회
    public List<Notification> findNotificationByUserIdAndRead(User activeUser) {
        // 읽음처리
        List<Notification> list = notificationRepository.findByReceiver(activeUser);
        //for(Notification notification : list) notification.setIsRead(true);
        // 알림목록 리턴
        return list;
    }

    // 알림 삭제
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    //댓글, 대댓글 알림 저장
    public void saveReplyNotification(Reply reply, User sender) throws IOException {
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
            Notification notification = Notification.builder().sender(sender).receiver(receiver).notificationType(type)
                    .cdate(new Date()).title(sliceText(reply.getOrigin().getText()))
                    .url(url).isRead(false).build();
            notificationRepository.save(notification);
            sendEmitter(notification, receiver);
        }
    }


    //너무 긴 내용 잘라주는 메소드
    public String sliceText(String text){
        if(text.length() > 10){
            text = text.substring(0,9)+"...";
        }
        return text;
    }

    // 위클리 태그 알림 저장
    public void saveTagNotification(Weekly weekly, User sender) throws IOException {

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
            sendEmitter(notification, go.getUser());
        }

    }
    
    //팔로우알림 저장
    public void saveFollowNotification(User receiver, User sender) throws IOException {
        Notification notification = Notification.builder()
                .sender(sender)
                .receiver(receiver)
                .notificationType("follow")
                .cdate(new Date())
                .title(null)
                .url("/member/"+sender.getEmail())
                .isRead(false)
                .build();
        notificationRepository.save(notification);
        sendEmitter(notification, receiver);
    }


    // SSE로 실시간 알림 전송
    private void sendEmitter(Notification notification, User receiver) throws IOException {
        SseEmitter sseEmitter = NotificationController.sseEmitters.get(receiver.getId());
        try {
            sseEmitter.send(SseEmitter.event().name("new").data( notification.changeJsonData(), MediaType.APPLICATION_JSON));
        } catch (Exception e) {
            e.printStackTrace();
            NotificationController.sseEmitters.remove(receiver.getId());
        }
    }

    // 해당 사용자의 모든 알림 읽음처리
    public void readAll(User user) {
        List<Notification> list = notificationRepository.findByReceiver(user);
        for(Notification notification : list) {
            if(!notification.getIsRead()) notification.setIsRead(true);
        }
    }
}
