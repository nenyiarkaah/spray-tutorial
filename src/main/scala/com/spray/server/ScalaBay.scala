package com.spray.server

import akka.actor.{Actor, ActorSystem, Props}
import com.spray.models.{MultiCrystalSilicon, Silicon}
import spray.http.MediaTypes
import spray.routing.{RequestContext, Route, SimpleRoutingApp}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._

object ScalaBay extends App with SimpleRoutingApp {
  implicit val actorSystem = ActorSystem()
  import actorSystem.dispatcher
  implicit val timeout = Timeout(1.second)

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

  lazy val helloActor = actorSystem.actorOf(Props(new HelloActor()))
  lazy val burnActor = actorSystem.actorOf(Props(new BurnActor()))

  lazy val helloRoute = get {
    path("hello") {
//      complete { "Welcome to Silicon Valley!" }
      //         directives
              ctx => ctx.complete("Welcome to Silicon Valley!")
    }
  }

  lazy val helloRoute2 = get {
    path("hello2") {
      ctx => helloActor ! ctx
    }
  }

  lazy val burnRoute = get {
    path("burn" / "remaining") {
      complete {
        (burnActor ? RemainingBrutningTime)
          .mapTo[Int]
          .map(s => s"The remaining burning time is $s")
      }
    }
  }

  startServer(interface = "localhost", port = 8080) {
    helloRoute ~
      helloRoute2 ~
    burnRoute ~
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
        parameters("name" ?, "grainSize".as[Int]) { (name, grainSize) =>
          val newSilicon = MultiCrystalSilicon(name.getOrElse("Multicrystalline"), grainSize)
          plentyOfSilicon = newSilicon :: plentyOfSilicon
          complete {
            "Ok"
          }
        }
      }
    }
  }


  class HelloActor extends Actor {
    override def receive = {
      case ctx: RequestContext => ctx.complete("Hello to Silicon Valley 2!")
    }
  }

  class BurnActor extends Actor {
    val remaningTime = 10

    override def receive = {
      case RemainingBrutningTime => sender ! remaningTime
    }
  }

  object RemainingBrutningTime

}


