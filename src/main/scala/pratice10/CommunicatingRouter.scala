package pratice10

import akka.actor.{Actor, ActorRef, ActorSystem, Kill, Props}
import pratice8.StringPrinter

import scala.collection.immutable

/**
  * Actor通讯-Actor通讯-转发模式
  *
  */
class Router extends Actor {
  var i = 0
  val children: immutable.IndexedSeq[ActorRef] = for (_ <- 0 until 4) yield context.actorOf(Props[StringPrinter])

  override def receive: Receive = {
    case msg =>
      //转发消息
      children(i).forward(msg)
      i = (i + 1) % 4
    case "stop" => context.stop(self)
  }
}

object CommunicatingRouter extends App {
  val ourSystem = ActorSystem("communicatingRouterTest")
  val router = ourSystem.actorOf(Props[Router])
  router ! "Hi."
  router ! "I'm talking to you!"
  Thread.sleep(1000)
  router ! "stop"
  ourSystem.terminate()
}
