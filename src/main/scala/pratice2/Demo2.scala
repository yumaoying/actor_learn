package pratice2

import akka.actor.{ActorSystem, Props}

/**
  * Created by yumaoying
  * 相互发送消息
  *
  */
object Demo2 extends App {
  //给pongActor发送一个开始消息
  //pongActor收到一个开始消息后会给ping发送一个pong消息
  //pingActor收到pong消息后会回复一个ping消息
  val system = ActorSystem("ping_pong")
  val pingActor = system.actorOf(Props[PingActor], name = "ping")
  val pongActor = system.actorOf(Props(new PongActor(pingActor)), name = "pong")
  pongActor ! StartMessage
}
