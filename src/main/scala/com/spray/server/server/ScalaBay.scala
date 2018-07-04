package com.spray.server.server

import akka.actor.ActorSystem
import com.spray.models.Silicon
import spray.http.MediaTypes
import spray.routing.SimpleRoutingApp

object ScalaBay extends App with SimpleRoutingApp {
  implicit val actorSystem = ActorSystem()

  // type Route = RequestContext => Unit
  // directives
  val plentyOfSilicon = Silicon.silicons

  startServer(interface = "localhost", port = 8080) {
    get {
      path("hello") {
        complete { "Welcome to Silicon Valley!" }
//         directives
//        ctx => ctx.complete("Welcome to Silicon Valley!")
      }
    } ~
    get {
      path("list" / "all") {
        respondWithMediaType(MediaTypes.`application/json`) {
          complete { Silicon.toJson(plentyOfSilicon) }
        }
      }
    }
  }
}


