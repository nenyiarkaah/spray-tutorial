package com.spray.server

import org.scalatest.{FlatSpec, FunSuite, ShouldMatchers}
import spray.routing.Directives
import spray.testkit.ScalatestRouteTest

class ScalaBayTest extends FlatSpec with ShouldMatchers with ScalatestRouteTest with Directives {

  it should "work" in {
    Get("/hello") ~> ScalaBay.helloRoute ~> check {
      responseAs[String] should include ("Silicon")
    }
  }
}
