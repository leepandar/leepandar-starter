package com.leepandar.starter.message.websocket.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import com.leepandar.starter.message.websocket.dao.WebSocketSessionDao;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * WebSocket 工具类
 */
public class WebSocketUtils {

    private static final Logger log = LoggerFactory.getLogger(WebSocketUtils.class);
    private static final WebSocketSessionDao SESSION_DAO = SpringUtil.getBean(WebSocketSessionDao.class);

    private WebSocketUtils() {
    }

    /**
     * 发送消息
     *
     * @param clientId 客户端 ID
     * @param message  消息内容
     */
    public static void sendMessage(String clientId, String message) {
        WebSocketSession session = SESSION_DAO.get(clientId);
        sendMessage(session, message);
    }

    /**
     * 发送消息
     *
     * @param session 会话
     * @param message 消息内容
     */
    public static void sendMessage(WebSocketSession session, String message) {
        sendMessage(session, new TextMessage(message));
    }

    /**
     * 批量发送消息
     *
     * @param clientIds 客户端 ID 列表
     * @param message   消息内容
     * @since 2.12.1
     */
    public static void sendMessage(List<String> clientIds, String message) {
        Collection<String> sessionIds = CollUtil.intersection(SESSION_DAO.listAllSessionIds(), clientIds);
        sessionIds.parallelStream().forEach(sessionId -> sendMessage(sessionId, message));
    }

    /**
     * 发送消息给所有客户端
     *
     * @param message 消息内容
     * @since 2.12.1
     */
    public static void sendMessage(String message) {
        SESSION_DAO.listAll().forEach(session -> sendMessage(session, message));
    }

    /**
     * 发送消息
     *
     * @param session 会话
     * @param message 消息内容
     */
    public static void sendMessage(WebSocketSession session, WebSocketMessage<?> message) {
        if (session == null || !session.isOpen()) {
            log.warn("WebSocket session closed.");
            return;
        }
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            log.error("WebSocket send message failed. sessionId: {}.", session.getId(), e);
        }
    }
}
