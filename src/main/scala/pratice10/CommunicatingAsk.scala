package pratice10

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.util.Timeout
import scala.concurrent.duration._
import akka.pattern.{ask, pipe}
import scala.concurrent.ExecutionContext.Implicits.global

/** *
  * Actor通讯-Actor通讯-请求模式
  */
class Pongy extends Actor {

  def receive: Receive = {
    case "ping" =>
      println("Got a ping -- ponging back!")
      sender ! "pong"
      context.stop(self)
  }

  override def postStop(): Unit = {
    println("pongy going down")
  }
}


class Pingy extends Actor {
  def receive: Receive = {
    case pongyRef: ActorRef =>
      implicit val timeout: Timeout = Timeout(2.seconds)
      val future = pongyRef ? "ping"
      //用pipeTo组合子，将Future对象的值发送给Actor对象的引用pongyRef的发送者
      pipe(future) to sender
  }
}


object CommunicatingAsk extends App {
  val ourSystem = ActorSystem("communicatingAskTest")
  val masta = ourSystem.actorOf(Props[Master], "masta")
  masta ! "start"
  Thread.sleep(1000)
  ourSystem.terminate()


  class Master extends Actor {
    val pingy: ActorRef = context.actorOf(Props[Pingy], "pingy")
    val pongy: ActorRef = context.actorOf(Props[Pongy], "pongy")

    def receive: Receive = {
      case "start" =>
        pingy ! pongy
      case "pong" =>
        println("got a pong back!")
        context.stop(self)
    }

    override def postStop(): Unit = {
      println("master going down")
    }
  }

}