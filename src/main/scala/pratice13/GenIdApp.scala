package pratice13

import akka.actor.{ActorSystem, Props}

import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import akka.pattern._
import akka.util.Timeout

import scala.concurrent.duration._

/**
  * Created by yumaoying
  * DisPatcher的使用
  */
object GenIdApp extends App {
  val actorSystem = ActorSystem("genIdActorTest")
  val genIDActor = actorSystem.actorOf(Props(new GenIdActor),"genIDActor")

  def genID: Future[String] = {
    //设置超时时间
    implicit val timeout: Timeout = Timeout(20.seconds)
    (genIDActor ? "genID").asInstanceOf[Future[String]]
  }

  //分配器实现了ExecutionContext接口，因此可以用来运行Future调用等待。
  implicit val ex:ExecutionContextExecutor = actorSystem.dispatcher
  val getID = for {
    //Future执行需要一个隐式参数ExecutionContext，可以直接用actor的dispacher
    _ <- Future(println("======================"))
    id <- genID
  } yield id
  val rs = Await.result(getID, 1.seconds)
  println(rs)
  actorSystem.terminate()
}

