package com.example

import com.example.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import com.example.plugins.routing.configureRouting
import org.jetbrains.exposed.sql.Database


fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        configureRouting()
        configureSecurity()
        configureMonitoring()
        configureTemplating()
        configureSerialization()

    }.start(wait = true)
}

