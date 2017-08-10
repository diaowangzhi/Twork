package com.twork.websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/ws/websocket")
public class WebSocket {

    private static final Set<WebSocket> connections = new CopyOnWriteArraySet<>();
    private static int onlineCount = 0;
    private static Map<Integer, WebSocket> map = new HashMap<>();
    private Session session;

    private static void broadcast(String msg) {
        for (WebSocket client : connections) {
            try {
                synchronized (client) {
                    client.session.getBasicRemote().sendText(msg);
                }
            } catch (IOException e) {
                connections.remove(client);
                System.out.println(e);
                e.printStackTrace();
                try {
                    client.session.close();
                } catch (IOException e1) {
                    System.out.println(e1);
                    e1.printStackTrace();
                }
            }
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        connections.add(this);
        broadcast(this.session + " | connect");
    }

    @OnClose
    public void onClose() {
        connections.remove(this);
        broadcast(this.session + " | disconnect");
    }

    @OnMessage
    public void onMessage(String message) {
        broadcast(this.session + " | " + message);
    }

    @OnError
    public void onError(Throwable t) {
        System.out.println(t);
    }
}
