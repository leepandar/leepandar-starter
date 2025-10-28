package com.leepandar.starter.message.websocket.dao;

import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.Set;

/**
 * WebSocket 会话 DAO
 */
public interface WebSocketSessionDao {

    /**
     * 添加会话
     *
     * @param key     会话 Key
     * @param session 会话信息
     */
    void add(String key, WebSocketSession session);

    /**
     * 删除会话
     *
     * @param key 会话 Key
     */
    void delete(String key);

    /**
     * 获取会话
     *
     * @param key 会话 Key
     * @return 会话信息
     */
    WebSocketSession get(String key);

    /**
     * 获取所有会话
     *
     * @return 所有会话
     * @since 2.12.1
     */
    Collection<WebSocketSession> listAll();

    /**
     * 获取所有会话 ID
     *
     * @return 所有会话 ID
     * @since 2.12.1
     */
    Set<String> listAllSessionIds();
}
