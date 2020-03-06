package pratice5

import akka.actor.{Actor, ActorSystem, Props}

/**
  * 管理无法处理正常处理的消息
  * ------当Actor实例收到无法由其Actor处理的消息时，该消息会被存储在UnhandledMessage对象中并转发给Actor系统的事件流
  * 通常，Actor系统的事件流负责记录日志
  */
class DeafActor extends Actor {

  //receive方法返回空的偏函数，这个空的偏函数不是为任何类型的消息定义的，
  // 因此DefActor收到任何消息都会被发送给unhandled方法
  override def receive: Receive = PartialFunction.empty

  //默认情况下，unhandled方法会在Actor系统的事件流中发布无法正常处理的消息
  // 通过重写这个unhandled方法可以修改默认处理方式
  override def unhandled(message: Any): Unit = message match {
    case msg: String => println(s"I do not hear $msg")
    case msg => super.unhandled(msg)
  }
}


object DefActorTest extends App {
  val system = ActorSystem("defActorTest")
  val deafActor = system.actorOf(Props[DeafActor], name = "deafActor")
  //被unhandled方法捕获，显示
  deafActor ! "ha"
  Thread.sleep(1000)
  //转发给Actor系统的事件流，未显示
  deafActor ! 1234
  Thread.sleep(1000)
  system.terminate()
}