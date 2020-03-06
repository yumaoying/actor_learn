package pratice1

import akka.actor.Actor

/**
  * Actor类设置了Actor实例的行为:
  * 设置Actor实例回应消息和与其他Actor实例通信的方式，
  * 并封装了Actor实例的状态，
  * 定义了运行和关闭Actor实例的次序
  */
class HelloACtor extends Actor {
  //重写接受消息的偏函数，其功能是接受消息并处理
  //receive方法定义了一个类型为PartialFunction[Any, Unit]的偏函数，
  //当Actor实例接受到Any类型的消息时，该偏函数就会被用到,如果没有为消息定义偏函数，那么消息就会被抛弃
  override def receive: Receive = {
    case "Hello" =>
      println("你好!")
    case "Stop" =>
      //self是对自己的引用
      context.stop(self) //停止自己的actorRef
      //context.system 是Actor系统的引用
      context.system.terminate() //关闭ActorSystem,即关闭其内部的线程池(ExcutorService)
    case _ =>
      println("你是？")
  }
}