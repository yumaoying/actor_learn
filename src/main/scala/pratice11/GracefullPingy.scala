package pratice11

import akka.actor.{Actor, ActorSystem, Props, Terminated}
import pratice10.Pongy
import akka.pattern._
import scala.concurrent.duration._
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Actor停止
  * 当需要从Actor对象内部跟踪停止Actor对象时，可以使用停止监视模式
  */
class GracefullPingy extends Actor {

  val pongy = context.actorOf(Props[Pongy], "pongy")
  //调用context对象的watch方法，这可以确保Pongy对象停止运行和postStop方法执行完毕后，
  //GracefullPingy能收集到Pongy对象引用的Terminated消息
  context.watch(pongy)

  override def receive: Receive = {
    case "Die,Pingy!" =>
      println("get msg Die,Pingy!")
      context.stop(pongy)
    case Terminated(`pongy`) =>
      //收到Terminated消息后会停止本身的运行
      println("stop GracefullPingy")
      context.stop(self)
  }
}


object CommunicationGracefullStop extends App {
  val ourSystem = ActorSystem("communicationGracefullStopTest")
  val grace = ourSystem.actorOf(Props[GracefullPingy], "grace")
  //akka.pattern的gracefullStop方法能够在接受到Actor对象引用，超时和关闭类型的消息，
  // 该方法会返回一个Future对象并以异步方式向Actor对象发送关闭消息。
  val stopped = gracefulStop(grace, 3.seconds, "Die,Pingy!")
  stopped onComplete {
    case Success(_) =>
      println("graceful shutdown successful")
      ourSystem.terminate()
    case Failure(_) =>
      println("grace not stopped!")
      ourSystem.terminate()
  }
}