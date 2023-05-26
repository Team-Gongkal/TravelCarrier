package tc.travelCarrier.socket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;
import tc.travelCarrier.socket.ReplyEchoHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ReplyEchoHandler(), "/replyEcho").setAllowedOrigins("*");
    }
}