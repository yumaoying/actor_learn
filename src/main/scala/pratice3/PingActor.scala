package pratice3

import akka.actor._

/** *
  * PingActor不主动发送消息，仅收到消息后回复
  *
  * pingActor收到一个Pong的消息后回复一个ping消息
  */
class PingActor extends Actor {

  override def receive: Receive = {
    case PongMessage => println("PingActor Receive a PongMessage")
      sender ! PingMessage
    case StopMessage =>
      println("PingActor Receive a StopMessage")
      //将自己停止
      context.stop(self)
  }
}

/** *
  * PongActor会给pingActor发送消息，且会处理ping消息(创建时需要pingActor的引用)
  *
  * 在收到一个start消息后，给ping发送一个pong消息
  * 在接受到ping消息后打印
  */
class PongActor(pingActor: ActorRef) extends Actor {
  var count = 0

  def incrementAndPrint(): Unit = {
    count += 1
    println(s"PongActor with ping通讯次数:$count ")
  }

  override def receive: Receive = {
    case PingMessage =>
      println("PongActor Receive a PingMessage")
      if (count > 9) {
        sender() ! StopMessage
        println("PongActor count 大于9,stop PongActor")
        context.stop(self)
      } else {
        incrementAndPrint()
        sender() ! PongMessage
      }
    case StartMessage =>
      println("PongActor Receive a StartMessage")
      pingActor ! PongMessage
  }
}

case object PingMessage

case object PongMessage

case object StartMessage

case object StopMessage