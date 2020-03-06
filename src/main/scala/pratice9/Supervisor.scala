package pratice9

import akka.actor.SupervisorStrategy.{Escalate, Restart}
import akka.actor.{Actor, ActorKilledException, ActorRef, ActorSystem, Kill, OneForOneStrategy, Props}

/** *
  * 监控策略
  */
class Naughty extends Actor {

  def receive: Receive = {
    case s: String => println(s"receive $s")
    case _ => throw new RuntimeException
  }

  override def postRestart(t: Throwable): Unit = {
    println("naughty restarted")
  }
}

class Supervisor extends Actor {
  val child: ActorRef = context.actorOf(Props[Naughty], "victim")

  def receive: Receive = PartialFunction.empty

  override val supervisorStrategy: OneForOneStrategy =
    //OneForOneStrategy监控策略，当Actor对象失效时，根据导致该对象失效的异常，该对象会被恢复，重启，或停止
    //AllForOneStrategy也有类似效果，但是应用于所有的子Actor对象
    OneForOneStrategy() {
      case _: ActorKilledException => Restart
      case _ => Escalate
    }
}


object SupervisionKill extends App {
  val ourSystem = ActorSystem("supervisorTest")
  val s = ourSystem.actorOf(Props[Supervisor], "super")
  ourSystem.actorSelection("/user/super/*") ! Kill   //发送kill消息，让Naughty对象失效，但super会根据重启策略来重启actor
  ourSystem.actorSelection("/user/super/*") ! "sorry about that"  //发送字符串消息,能正常处理
  ourSystem.actorSelection("/user/super/*") ! "kaboom".toList  // 不能正常处理，两个对象都会停止
  Thread.sleep(1000)
  ourSystem.stop(s)
  Thread.sleep(1000)
  ourSystem.terminate()
}
