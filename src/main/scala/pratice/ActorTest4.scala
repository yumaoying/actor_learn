package pratice

import scala.actors.Actor

case class MorningGreet(name: String, greet: String)

case class WorkingInfo(content: String)

/**
  * 两个Actor双向通讯
  * 两个Actor之间相互发送消息，一个发送并接收，然后再发送给另一个，另一个在接收消息。
  */
object ActorTest4 {
  def main(args: Array[String]): Unit = {
    val pActor = new ProgrammerActor
    val mActor = new ManagerActor(pActor)
    pActor.start()
    mActor.start()
    val meeting = "~每日晨会~"
    mActor ! meeting
  }
}


//经理Actor
class ManagerActor(pActor: ProgrammerActor) extends Actor {
  override def act(): Unit = {
    while (true) {
      receive {
        case meeting: String =>
          println("开会通知:" + meeting)
          //向同事们问候
          pActor ! MorningGreet("hello", "大家早上好")
        case MorningGreet(name, greet) =>
          //开会内容是什么
          println("程序猿说:" + name + "," + greet)
          pActor ! WorkingInfo("每个人请报一下昨天工作的内容~")
      }
    }
  }
}

//程序员Actor
class ProgrammerActor() extends Actor {
  override def act(): Unit = {
    while (true) {
      receive {
        case MorningGreet(name, greet) =>
          println("项目经理说:" + name + "," + greet)
          //向manager也礼貌性的回复一下 此时的sender就是MorningGreet消息的来源
          sender ! MorningGreet("hi", "经理好")
        case WorkingInfo(content) =>
          println(content)
      }
    }
  }
}