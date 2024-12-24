package com.flowery.flowerygateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FloweryGatewayApplication

fun main(args: Array<String>) {
    runApplication<FloweryGatewayApplication>(*args)
}
