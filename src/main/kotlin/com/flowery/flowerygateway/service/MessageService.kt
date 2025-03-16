package com.flowery.flowerygateway.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.flowery.flowerygateway.dto.Message
import com.flowery.flowerygateway.lib.WebSocketHandler
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import java.util.concurrent.CompletableFuture


@Service("messageService")
class MessageService {
    private val logger = LoggerFactory.getLogger(MessageService::class.java)
    private val wsUrl = "ws://localhost:8001/ws/"
    var session: WebSocketSession? = null
    private val mapper = jacksonObjectMapper()

    fun connect() {
        val client = StandardWebSocketClient()
        val handler = WebSocketHandler(this)
        val futureSession = client.execute(handler, wsUrl)

        CompletableFuture.runAsync {
            try {
                session = futureSession.get()
                logger.info("WebSocket session established")
            } catch (e: Exception) {
                logger.error("Exception in WebSocket connect: ", e)
            }
        }
    }

    fun sendMessage(message: Message) {
        session?.let {
            if (it.isOpen) {
                val jsonMessage = mapper.writeValueAsString(message)
                it.sendMessage(TextMessage(jsonMessage))
            } else {
                logger.warn("WebSocket session is closed")
            }
        } ?: logger.warn("WebSocket session is not established")
    }

    fun disconnect() {
        session?.let {
            if (it.isOpen) {
                try {
                    it.close()
                    logger.info("WebSocket session closed")
                } catch (e: Exception) {
                    logger.error("Exception in WebSocket disconnect: ", e)
                }
            }
        } ?: logger.warn("No active WebSocket session to close")
    }
}