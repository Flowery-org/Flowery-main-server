package com.flowery.flowerygateway.lib

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.flowery.flowerygateway.dto.Message
import com.flowery.flowerygateway.service.MessageService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class WebSocketHandler(private val clientService: MessageService): TextWebSocketHandler() {
    private val logger = LoggerFactory.getLogger(WebSocketHandler::class.java)
    private val mapper = jacksonObjectMapper()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        clientService.session = session
        logger.info("Connected to WebSocket server: ${session.uri}")
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        logger.info("WebSocket connection closed: $status")
        clientService.session = null
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val msgData = mapper.readValue(message.payload, Message::class.java)
        logger.info("Received message: $msgData")
    }
}