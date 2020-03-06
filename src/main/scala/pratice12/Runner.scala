package pratice12

import akka.actor.{Actor, ActorIdentity, Identify, Props}
import pratice10.Pingy

//1.实例化ping对象，并获取pong对象的引用，将该引用交给ping对象，
//Actor系统配置的Runner对象并不具备位置透明性，这是分布式系统中常见的情况;
//程序中的某个部分负责寻找远程Actor系统中的Actor对象，并对这些Actor对象进行初始化，而应用程序本身的逻辑封装在其他独立运行的Actor对啊ing中
class Runner extends Actor {

  //创建ping的引用
  val pingy = context.actorOf(Props[Pingy], "pingy")

  def receive: Receive = {
    case "start" =>
      //收到消息后会为pong对象构造actor路径，使用tcp路径和actro系统的名称，一及远程计算机的地址，端口
      //发送Identify消息，获取pong对象的引用
      val path = context.actorSelection("akka.tcp://PongyDimension@127.0.0.1:24321/user/pongy")
      path ! Identify(0)
    case ActorIdentity(0, Some(ref)) =>
      //当pong的引用发送给ping后，就可以通讯了
      pingy ! ref
    case ActorIdentity(0, None) =>
      println("Something's wrong -- no pongy anywhere!")
      context.stop(self)
    case "pong" =>
      println("got a pong from another dimension.")
      context.stop(self)
  }
}