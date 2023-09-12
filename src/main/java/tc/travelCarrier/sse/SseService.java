package tc.travelCarrier.sse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import tc.travelCarrier.domain.Notification;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.web.NotificationController;

import java.io.IOException;

@Service
public class SseService {

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


    // SSE로 실시간 알림 전송
    public void sendEmitter(Notification notification, User receiver) throws IOException {
        SseEmitter sseEmitter = NotificationController.sseEmitters.get(receiver.getId());
        try {
            sseEmitter.send(SseEmitter.event().name("new").data( notification.changeJsonData(), MediaType.APPLICATION_JSON));
        } catch (Exception e) {
            e.printStackTrace();
            NotificationController.sseEmitters.remove(receiver.getId());
        }
    }
}
