package tc.travelCarrier.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tc.travelCarrier.domain.*;
import tc.travelCarrier.repository.MemberRepository;
import tc.travelCarrier.repository.NotificationRepository;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;

    //전체 알림 조회
    public List<Notification> findNotificationByUserId(User activeUser) {
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
    public Notification saveReplyNotification(Reply reply, User sender) throws IOException {
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
                    .cdate(new Date()).title(sliceText(reply.getOrigin()==null? reply.getAttachDaily().getTitle() : reply.getOrigin().getText()))
                    .url(url).isRead(false).build();
            notificationRepository.save(notification);
            return notification;
            //sendEmitter(notification, receiver);
        }
        return null;
    }


    //너무 긴 내용 잘라주는 메소드
    public String sliceText(String text){
        if(text.length() > 10){
            text = text.substring(0,9)+"...";
        }
        return text;
    }

    // 위클리 태그 알림 저장
    public Notification[] saveTagNotification(Weekly weekly, User sender) throws IOException {
        Notification[] noti = new Notification[weekly.getGowiths().size()];

        int i=0;
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
            noti[i++] = notification;
            //sendEmitter(notification, go.getUser());
        }
        return noti;
    }
    
    //팔로우알림 저장
    public Notification saveFollowNotification(User receiver, User sender) throws IOException {
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

        return notification;
        //sendEmitter(notification, receiver);
    }



    // 해당 사용자의 모든 알림 읽음처리
    public void readAll(User user) {
        List<Notification> list = notificationRepository.findByReceiver(user);
        for(Notification notification : list) {
            if(!notification.getIsRead()) notification.setIsRead(true);
        }
    }


    public boolean findNotificationByUserIdAndIsRead(User user, boolean b) {
        List<Notification> list = notificationRepository.findByReceiverAndIsRead(user,b);
        if(list.size()==0) return false;
        return true;
    }
}
