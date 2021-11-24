package com.senzhikong.web.websocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author Shu.zhou
 * @date 2018年12月3日下午3:20:15
 */
public class BaseTextSocketHandler extends TextWebSocketHandler {

    public BaseTextSocketHandler() {
    }

    public static void sendMessageToUser(WebSocketSession user, TextMessage message) {
        if (user != null && user.isOpen()) {
            try {
                user.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     */
    public static void sendMessageToUsers(List<WebSocketSession> users, TextMessage message) {
        Iterator<WebSocketSession> it = users.iterator();
        if (it.hasNext()) {
            WebSocketSession user = it.next();
            try {
                if (user.isOpen()) {
                    user.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
