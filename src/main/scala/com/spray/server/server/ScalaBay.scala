package com.spray.server.server

import akka.actor.ActorSystem
import spray.routing.SimpleRoutingApp

object ScalaBay extends App with SimpleRoutingApp {
  implicit val actorSystem = ActorSystem()

  startServer(interface = "localhost", port = 8080) {
    get {
      path("hello") {
        complete { "Welcome to Silicon Valley!" }
      }
    }
  }
}


