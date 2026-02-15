package com.paveltsikota.webcore.websocket.handler

import com.paveltsikota.webcore.service.job.controller.TimerCoroutineService

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import org.springframework.stereotype.Component
import org.springframework.web.socket.*
import org.springframework.web.socket.handler.TextWebSocketHandler

@Deprecated(message = "Only 4 testing")
@Component
class TimerStatusWebSocketHandler(
    private val timerService: TimerCoroutineService
) : TextWebSocketHandler() {

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val scope = CoroutineScope(Dispatchers.Default)

        val job = scope.launch {
            timerService.status.collectLatest { status ->
                if (session.isOpen) {
                    session.sendMessage(TextMessage(status))
                }
            }
        }

        session.attributes["job"] = job
    }

    override fun afterConnectionClosed(
        session: WebSocketSession,
        status: CloseStatus
    ) {
        (session.attributes["job"] as? Job)?.cancel()
    }
}
