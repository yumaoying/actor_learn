package pratice13

import akka.actor.{ActorSystem, Props}
import akka.pattern._
import akka.util.Timeout
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor, Future}

/**
  * Created by yumaoying
  * 自定义的DisPatcher
  */
object GenIdApp2 extends App {
  //通过配置文件来配置DisPatcher
  //  val actorSystem = ActorSystem.create("genIdActorTest2")
  val actorSystem = ActorSystem.create("genIdActorTest2",
    ConfigFactory.load("application.conf"))

  //自定义的disPatcher
  val genIDActor = actorSystem.actorOf(Props(new GenIdActor).withDispatcher("my-dispatcher"), "genIDActor2")


  implicit val ex: ExecutionContextExecutor = actorSystem.dispatchers.lookup("my-dispatcher")
  //查找分配器,
  //分配器实现了ExecutionContext接口，因此可以用来运行Future调用等待。
  //  implicit val ex: ExecutionContextExecutor = actorSystem.dispatcher

  val getID = for {
    //Future执行需要一个隐式参数ExecutionContext，可以直接用actor的dispacher
    _ <- Future(println("thread name:" + Thread.currentThread().getName))
    id <- genID
  } yield id

  val rs = Await.result(getID, 1.seconds)
  println(rs)

  actorSystem.terminate()

  def genID: Future[String] = {
    //设置超时时间
    implicit val timeout: Timeout = Timeout(20.seconds)
    (genIDActor ? "genID").asInstanceOf[Future[String]]
  }


}
