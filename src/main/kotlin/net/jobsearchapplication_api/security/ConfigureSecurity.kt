package net.jobsearchapplication_api.security

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.response.*
import net.jobsearchapplication_api.base.BaseResponse
import net.jobsearchapplication_api.config.INVALID_AUTHENTICATION_TOKEN
import java.util.UUID

fun Application.configureSecurity() {
    JwtConfig.initialize("my-story-app")
    install(Authentication) {
        jwt {
            verifier(JwtConfig.instance.verifier)
            validate {
                val claim = it.payload.getClaim(JwtConfig.CLAIM).asString()
                if (claim != null) {
                    val userId = UUID.fromString(claim)
                    UserIdPrincipalForUser(userId)
                } else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(
                    status = HttpStatusCode.Unauthorized,
                    message = BaseResponse.ErrorResponse(INVALID_AUTHENTICATION_TOKEN)
                )
            }
        }
    }
}