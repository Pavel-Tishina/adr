package com.paveltsikota.webcore.websocket.handler

import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Component
class TimeWebSocketHandler : TextWebSocketHandler() {

    private val sessions = ConcurrentHashMap.newKeySet<WebSocketSession>()
    private val scheduler = Executors.newSingleThreadScheduledExecutor()

    init {
        scheduler.scheduleAtFixedRate({
            val time = LocalDateTime.now().toString()
            val message = TextMessage(time)

            sessions.forEach { session ->
                if (session.isOpen) {
                    session.sendMessage(message)
                }
            }
        }, 0, 1, TimeUnit.SECONDS)
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessions.add(session)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.remove(session)
    }
}
