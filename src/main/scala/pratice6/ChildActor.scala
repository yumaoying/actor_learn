package pratice6

import akka.actor.{Actor, ActorSystem, Props}

/**
  * Created by yumaoying
  * 测试Actor的层次结构
  *
  */
class ChildActor extends Actor {

  /** *
    * ChildActor通过将sayHi消息中的存储的引用发送给它的父类实例，对sayHi小心做出回应，
    * 父Actor类实例回通过调用context对象中的parsent方法，获取这个引用
    */
  override def receive: Receive = {
    case "sayHi" =>
      val parent = context.parent
      println(s"parent $parent to  me say hi")
  }

  //重写postStop方法，在ChildActor对象停止运行后被调用
  override def postStop(): Unit = {
    println("child stopped!")
  }
}


class ParentActor extends Actor {
  override def receive: Receive = {
    case "create" =>
      //当收到create消息时,调用context对象的actorOf创建新的子对象
      context.actorOf(Props[ChildActor])
      println(s"create a kid;children =${context.children}")
    case "sayHi" =>
      //当收到sayhi消息时，通过遍历context.children列表并向每个子对象发送消息，将消息传递给它的子对象
      println("kids,say hi!")
      for (c <- context.children) {
        c ! "sayHi"
      }
    case "stop" =>
      println("parent stopping!")
      //当收到stop消息时，停止自己的运行过程
      context.stop(self)
  }
}


object ActorsHierarchy extends App {
  val system = ActorSystem("hierarchyActorTest")
  //先创建父Actor引用，向parent发送两条create消息
  val parentActor = system.actorOf(Props[ParentActor], name = "parent")
  parentActor ! "create"
  parentActor ! "create"
  Thread.sleep(1000)
  parentActor ! "sayHi"
  Thread.sleep(1000)
  parentActor ! "stop"
  Thread.sleep(1000)
  system.terminate()


  //运行结果
  /** *
    * create a kid;children =ChildrenContainer(Actor[akka://hierarchyActorTest/user/parent/$a#-874564128])
    * create a kid;children =ChildrenContainer(Actor[akka://hierarchyActorTest/user/parent/$a#-874564128], Actor[akka://hierarchyActorTest/user/parent/$b#-1812241398])
    * kids,say hi!
    * parent Actor[akka://hierarchyActorTest/user/parent#-842633715] to  me say hi
    * parent Actor[akka://hierarchyActorTest/user/parent#-842633715] to  me say hi
    * parent stopping!
    * child stopped!
    * child stopped!
    */

  //分析
  /***
    * 1.当父Actor停止运行后，子Actor也会停止运行
    * 2.Actor对象引用反应了Actor对象在Actor层次结构中的位置
    *  akka://hierarchyActorTest/user/parent/$a
    *      -----akka:// 表明引用的是一个本地Actor对象
    *      -----hierarchyActorTest 本例Actor系统名称
    *      -----parent/$a 父actor对象的名称,和以自动方式为子Actor对象生成的子actor名称 $a
    */
}