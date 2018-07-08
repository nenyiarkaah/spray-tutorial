package com.spray.Client

import akka.actor.ActorSystem
import spray.http._
import spray.client.pipelining._

object ClientComplete extends App {
  implicit val system = ActorSystem()
  import system.dispatcher

  val pipeline = sendReceive

  val securePipeline = addCredentials(BasicHttpCredentials("adam", "1234")) ~> sendReceive

  val result = securePipeline(Get("http://localhost:8080/list/all"))
  result.foreach { response =>
    println(s"Requst completed with status ${response.status} and content:\n${response.entity.asString}")
  }

  pipeline(Post("http://localhost:8080/silicon/add/multi?name=macro&grainSize=10"))

  Thread.sleep(1000L)
  system.shutdown()
  system.awaitTermination()
}
