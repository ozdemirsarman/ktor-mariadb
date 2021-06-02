package com.example.plugins.routing


import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*


fun Application.configureRouting() {
    install(Locations) {
    }

    com.example.plugins.DBfunctions.initDB()

    routing {

        trace{
            application.log.trace(it.buildText())
        }

        route("/parameter", HttpMethod.Get) {
            param("name") {
                handle {
                    val name = call.parameters.get("name")
                    val surname = call.parameters.get("surname")
                    call.respondText("Dear $name $surname")
                }
            }

        }


        route("/parameter2", HttpMethod.Get) {

                handle {
                    val name = call.parameters.get("name")
                    val surname = call.parameters.get("surname")
                    call.respondText("Dear $name $surname")
                }
            }


        route("/createuser", HttpMethod.Get) {

            handle {

                    val name = call.parameters.get("name")
                    val surname = call.parameters.get("surname")

               // call.respondText("$name $surname user created.")
                call.respondText(com.example.plugins.DBfunctions.createuser(name))
            }
        }


        route("/createuser2", HttpMethod.Get) {

            handle {

                val username1 = call.parameters.get("username")
                val password1 = call.parameters.get("password")

                // call.respondText("$name $surname user created.")
                call.respondText(com.example.plugins.DBfunctions.createuser2(username1,password1))
            }
        }


        route("/weather") {
            get("/usa") {
                call.respondText("The weather in US : Snow")
            }
            route("/europe", HttpMethod.Get) {
                header("systemtoken", "weathersystem") {
                    param("name") {
                        handle {
                            val name = call.parameters.get("name")
                            call.respondText("Dear $name, The weather in Europe: sunny")
                        }
                    }
                    handle {
                        call.respondText("The weather in Europe: sunny")
                    }
                }
            }

        }

        post("/form") {
            val parameters = call.receiveParameters()
            parameters.names().forEach {
                val myvalue = parameters.get(it)
                log.info("key: $it, value: $myvalue")
            }
            call.respondText("Thank you for the form data")

        }


        get("/mariadb") {

            call.respondText(com.example.plugins.DBfunctions.getTopuserData())

        }

        get("/kullaniciekle") {

            call.respondText(com.example.plugins.DBfunctions.adduser())

        }

        get("/lastuser") {

            call.respondText(com.example.plugins.DBfunctions.getlastuser())

        }


        get("/usercount") {

            call.respondText(com.example.plugins.DBfunctions.usercount())

        }


        get("/") {
            call.respondText(text = "Hello World!")
        }
        get<MyLocation> {
            call.respondText("Location: name=${it.name}, arg1=${it.arg1}, arg2=${it.arg2}")
        }
        // Register nested routes
        get<Type.Edit> {
            call.respondText("Inside $it")
        }
        get<Type.List> {
            call.respondText("Inside $it")
        }
        // Static feature. Try to access `/static/ktor_logo.svg`
        static("/static") {
            resources("static")
        }
        install(StatusPages) {
            exception<AuthenticationException> { cause ->
                call.respond(HttpStatusCode.Unauthorized)
            }
            exception<AuthorizationException> { cause ->
                call.respond(HttpStatusCode.Forbidden)
            }

        }
    }
}

@Location("/location/{name}")
class MyLocation(val name: String, val arg1: Int = 42, val arg2: String = "default")
@Location("/type/{name}")
data class Type(val name: String) {
    @Location("/edit")
    data class Edit(val type: Type)

    @Location("/list/{page}")
    data class List(val type: Type, val page: Int)
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
