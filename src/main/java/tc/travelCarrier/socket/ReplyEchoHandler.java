package tc.travelCarrier.socket;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.repository.MemberRepository;
import org.apache.commons.lang3.StringUtils;
import tc.travelCarrier.security.SessionNames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ReplyEchoHandler extends TextWebSocketHandler {

    List<WebSocketSession> sessions = new ArrayList<>(); //서버에 접속중인 모든 세션
    Map<Integer, WebSocketSession> userSessions = new HashMap<>(); //로그인정보와 세션아이디를 함께 저장

    @Override
    // 웹소켓 커넥션이 연결되었을때, 즉 클라이언트가 웹서버에 접속했을때 실행
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("afterConnectionEstablished : "+session);
        sessions.add(session); //sessions에 현재 접속한 모든 세션들을 담는다.

/*        int loginUser = (((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
        System.out.println("map에 추가: " + loginUser+", "+session);
        userSessions.put(loginUser,session);*/
    }
    @Override
    // 소켓에 메세지를 보냈을때 실행
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("handleTextMessage: "+session +", "+message);
        // 프로토콜 : 타입 / 새댓글번호 / 알림보낸이 / 알림받는이 / 날짜 => (reply,101,1,2,2020-10-20)
        String msg = message.getPayload();
        System.out.println("message : "+message);
        if(StringUtils.isNotEmpty(msg)) {
            String[] strs = message.getPayload().split(",");
            if(strs != null && strs.length == 5){
                String type = strs[0];
                String id = strs[1];
                String sender = strs[2];
                String receiver = strs[3];
                String cdate = strs[4];

                WebSocketSession boardWriterSession = userSessions.get(receiver);
                if("reply".equals(type) && boardWriterSession != null){
                    TextMessage tmpMsg = new TextMessage(sender+"님이 "+id+"번 게시물에 댓글을 달았습니다.");
                    boardWriterSession.sendMessage(tmpMsg);
                }
            }
        }
    }


    @Override
    // 커넥션이 클로즈 됐을때 실행
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("afterConnectionClosed : "+session);
    }

}
