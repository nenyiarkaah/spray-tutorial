package com.spray.server.server

import akka.actor.ActorSystem
import com.spray.models.{MultiCrystalSilicon, Silicon}
import spray.http.MediaTypes
import spray.routing.{Route, SimpleRoutingApp}

object ScalaBay extends App with SimpleRoutingApp {
  implicit val actorSystem = ActorSystem()

  // type Route = RequestContext => Unit
  // directives
  var plentyOfSilicon = Silicon.silicons
  def getJson(route: Route): Route = {
    get {
      respondWithMediaType(MediaTypes.`application/json`) {
        route
      }
    }
  }

  lazy val helloRoute = get {
    path("hello") {
      complete { "Welcome to Silicon Valley!" }
      //         directives
      //        ctx => ctx.complete("Welcome to Silicon Valley!")
    }
  }

  startServer(interface = "localhost", port = 8080) {
     helloRoute ~
    getJson {
      path("list" / "all") {
        complete {
          Silicon.toJson(plentyOfSilicon)
        }
      }
    } ~
    get {
      path("silicon" / IntNumber / "details") { index =>
        complete { Silicon.toJson(plentyOfSilicon(index))}
      }
    } ~
    post {
      path("silicon" / "add") {
        parameters("name"?,"grainSize".as[Int]) { (name, grainSize) =>
          val newSilicon = MultiCrystalSilicon(name.getOrElse("Multicrystalline"), grainSize)
          plentyOfSilicon = newSilicon :: plentyOfSilicon
          complete{ "Ok" }
        }
      }
    }
  }
}


