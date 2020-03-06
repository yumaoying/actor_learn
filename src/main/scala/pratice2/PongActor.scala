package pratice2

import akka.actor._

/** *
  * PingActor不主动发送消息，仅收到消息后回复
  *
  * pingActor收到一个Pong的消息后回复一个ping消息
  */
class PingActor extends Actor {

  override def receive: Receive = {
    case PongMessage => println("Receive a PongMessage")
      sender ! PingMessage
  }
}

/** *
  * PongActor会给pingActor发送消息，且会处理ping消息(创建时需要pingActor的引用)
  *
  * 在收到一个start消息后，给ping发送一个pong消息
  * 在接受到ping消息后打印
  */
class PongActor(pingActor: ActorRef) extends Actor {
  override def receive: Receive = {
    case PingMessage =>
      println("Receive a PingMessage")
    case StartMessage =>
      println("Receive a StartMessage")
      pingActor ! PongMessage
  }
}

case object PingMessage

case object PongMessage

case object StartMessage

case object StopMessage