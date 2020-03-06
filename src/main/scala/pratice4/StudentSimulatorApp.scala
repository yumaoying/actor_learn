package pratice4

import akka.actor.{ActorRef, ActorSystem, Props}

/**
  * Created by yumaoying
  * 单向通讯
  * 　　1、Student创建了一些东西，称为ActorSystem；
  * 　　2、他用ActorSystem来创建一个ActorRef，并将QuoteRequest message发送到ActorRef 中（到达TeacherActor的一个代理）；
  * 　　3、ActorRef 将message单独传输到Dispatcher；
  * 　　4、Dispatcher将message按照顺序保存到目标Actor的MailBox中；
  * 　　5、然后Dispatcher将Mailbox放在一个Thread 中
  * 　　6、MailBox按照队列顺序取出消息，并最终将它递给真实的TeacherActor接受方法中。
  */
object StudentSimulatorApp extends App {
  //1.创建一个ActorSystem
  val actorSystem = ActorSystem("MessagesSystem")
  //2.为Teacher Actor创建代理（ActorRef为真实Actor的充当代理，客户端并不直接和Actor进行通信）
  val teacherActorRef: ActorRef = actorSystem.actorOf(Props[TeacherActor], "teacherActor")
  //3.将QuoteRequest message 发送到代理中
  teacherActorRef ! QuoteRequest
  Thread.sleep(1000)
  //关闭ActorSystem,旧版本是shutdown()
  actorSystem.terminate()
}
