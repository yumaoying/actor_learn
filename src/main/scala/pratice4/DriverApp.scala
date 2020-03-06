package pratice4

import akka.actor.{ActorRef, ActorSystem, Props}

/**
  * Created by yumaoying
  * 双向通讯
  *
  * 　　1、DriverApp个StudentActor发送了一个InitSignal消息；
  * 　　2、StudentActor对InitSignal消息作出反应，并且发送了QuoteRequest 消息给TeacherActor；
  * 　　3、TeacherActor用QuoteResponse作出了响应；
  * 　　4、StudentActor仅仅将QuoteResponse 作为日志打印到控制台/logger。
  */
object DriverApp extends App {
  //1.创建一个ActorSystem
  val actorSystem = ActorSystem("teacher_student")
  //2.创建teacher actor
  val teacherActor: ActorRef = actorSystem.actorOf(Props[TeacherActor], "teacherActor")
  //3.创建student actor
  val studentActor: ActorRef = actorSystem.actorOf(Props(new StudentActor(teacherActor)), "studentActor")
  //4.将student actor 发送message
  studentActor ! InitSignal
  Thread.sleep(1000)
  //关闭ActorSystem
  actorSystem.terminate()
}
