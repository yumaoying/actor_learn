package pratice3

import akka.actor.{ActorSystem, Props}

/**
  * Created by yumaoying
  * 做一个标识位，当ping-pong之前满足一定条件时，结束
  */
object Demo3 extends App {
  //给pongActor发送一个开始消息
  //pongActor收到一个开始消息后会给ping发送一个pong消息
  //pingActor收到pong消息后会回复一个ping消息
  //pongActor收到ping消息如果之间的通讯大于9次，会回复stop消息结束，否则会给pingActor回复pong消息
  val system = ActorSystem("ping_pong")
  val pingActor = system.actorOf(Props[PingActor], name = "ping")
  val pongActor = system.actorOf(Props(new PongActor(pingActor)), name = "pong")
  pongActor ! StartMessage
}
