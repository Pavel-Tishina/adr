package com.paveltsikota.webcore.websocket.config

import com.paveltsikota.webcore.websocket.handler.TimerStatusWebSocketHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketTimeConfig(
//    private val timeWebSocketHandler: TimeWebSocketHandler
    private val timeWebSocketHandler: TimerStatusWebSocketHandler
) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(timeWebSocketHandler, "/ws/time")
            .setAllowedOrigins("*") // для тестов
    }
}