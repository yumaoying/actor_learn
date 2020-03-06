package pratice7

import akka.actor.{Actor, ActorIdentity, ActorSystem, Identify, Props}

/**
  * Created by yumaoying on
  * Acotor路径
  * 在收到含有Actor路径的消息时，检查并显示Actor对象的引用。
  * 当CheckActor收到含有Actor路径或路径选择的字符串时，或获取ActorSelection对象，并向对象发送一条Identify消息
  * 这条消息会发送给路径选择中的所有Actor对象，而这些对象都会回复一条ActorIdentify消息
  *
  * 当CheckActor收到Identify消息时不是显示获得的Actor对象的引用，就是报告路径中不含该对象
  */
class CheckActor extends Actor {
  override def receive: Receive = {
    case path: String =>
      println(s"checking path $path")
      context.actorSelection(path) ! Identify(path)
    case ActorIdentity(path, Some(ref)) =>
      println(s"found actor $ref at $path")
    case ActorIdentity(path, None) =>
      println(s"could not found actor  at $path")
  }
}


object CheckActor extends App {
  val system = ActorSystem("checkActorTest")
  val checker = system.actorOf(Props[CheckActor], "check")
  //可以通过 path方式直接获取路径
  //  println(checker.path)
  // akka://checkActorTest/user/check
  // ../* 获取checker对象的父Actor对象的所有子Actor对象的引用:
  // cheker对象本身的引用及它同级Actor对象的引用
  checker ! "../*"
  //found actor Actor[akka://checkActorTest/user/check#-1851702317] at ../* -----本身的引用
  Thread.sleep(1000)
  //检查比checker对象高一级的所有Actor对象，守护者Actor对象user和system
  checker ! "../../*"
  //  found actor Actor[akka://checkActorTest/system] at ../../*
  //  found actor Actor[akka://checkActorTest/user] at ../../*
  Thread.sleep(1000)
  //绝对路径选择--守护者Actor对象的system(系统内置的Actor对象)
  checker ! "/system/*"
  //  found actor Actor[akka://checkActorTest/system/log1-Logging$DefaultLogger#-21119779] at /system/*  ----log1-Logging 用于记录日志
  //  found actor Actor[akka://checkActorTest/system/deadLetterListener#-375529312] at /system/*       ----deadLetterListener --处置无法正常处理的消息
  //  found actor Actor[akka://checkActorTest/system/eventStreamUnsubscriber-1#-1128593966] at /system/*
  Thread.sleep(1000)
  //不存在的路径
  checker ! "/user/checker2"
  //  could not found actor  at /user/checker2
  Thread.sleep(1000)
  system.stop(checker)
  Thread.sleep(1000)
  system.terminate()
}