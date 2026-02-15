package com.paveltsikota.webcore.websocket.config

import com.paveltsikota.webcore.websocket.handler.TimeWebSocketHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Deprecated(message = "Only 4 testing")
@Configuration
@EnableWebSocket
class WebSocketJobConfig(
    private val timeWebSocketHandler: TimeWebSocketHandler
) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
//        registry.addHandler(timeWebSocketHandler, "/ws/job")
        registry.addHandler(timeWebSocketHandler, "/ws/status")
            .setAllowedOrigins("*") // для тестов
    }
}