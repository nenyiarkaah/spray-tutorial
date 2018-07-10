package com.spray.models

import spray.json.DefaultJsonProtocol

trait MyJsonProtocol extends DefaultJsonProtocol {
  implicit val templateFormat = jsonFormat1(SiliconIndex)
}