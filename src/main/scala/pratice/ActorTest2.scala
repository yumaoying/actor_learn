package pratice

import scala.actors.Actor

//定义样例类，封装发送和接受消息类型
//同步消息
case class SyncMsg(id: Int, msg: String)

//异步消息
case class AsyncMsg(id: Int, msg: String)

//响应消息
case class ReplyMsg(id: Int, msg: String)

/** *
  * Actor各种发送消息操作符！，!?,!!的的使用
  * 结合case class样例类发送消息和接受消息
  * 将消息封装在一个样例类中
  * 通过匹配不同的样例类去执行不同的操作
  * Actor可以返回消息给发送方。通过sender方法向当前消息发送方返回消息
  */
object ActorTest2 extends App {
  val actor = new SecondActor
  actor.start()
  //  发送一个异步发送消息，无返回值消息
  //  val replay = actor ! AsyncMsg(1, "hello actor")
  //  println("异步发送消息完成")
  //  println(replay)

  //发送一个同步消息
  //  val replay2 = actor !? SyncMsg(2, "Hello Actor2")
  //  println("同步消息发送完成")
  //  println(replay2)

  //  发送一个异步带有返回值消息
  val replay3 = actor !! AsyncMsg(3, "Hello Actor3")
  println("异步消息发送完成")
  //检测返回结果是可用
  println(replay3.isSet)
  val res = replay3() //会阻塞，没有结果时会阻塞
  println(replay3.isSet)
  println(res)
}

class SecondActor extends Actor {
  override def act(): Unit = {
    while (true) {
      receive {
        case SyncMsg(id, msg) =>
          println(s"$id SyncMsg $msg")
          Thread.sleep(5000)
          sender ! ReplyMsg(id, "finished")
        case AsyncMsg(id, msg) =>
          println(s"$id AsyncMsg $msg")
          Thread.sleep(5000)
          sender ! ReplyMsg(id, "finished")
      }
    }
  }
}