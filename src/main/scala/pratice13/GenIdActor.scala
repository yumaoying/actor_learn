package pratice13

import akka.actor.Actor

/**
  * Created by yumaoying
  */
class GenIdActor extends Actor {
  var num = 0

  def genID: String = {
    if (num > 999999999) num = 1 else num += 1
    s"${num.formatted("%09d")}"
  }

  def receive: Receive = {
    case "genID" =>
      println("GenIdActor thread name:" + Thread.currentThread().getName)
      sender ! genID
  }
}
