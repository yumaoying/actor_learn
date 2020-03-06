package pratice8

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

/** *
  * Actor的生命周期
  */
object ActorsLifecycle extends App {
  val system = ActorSystem("lifecycleTest")
  val testy = system.actorOf(Props[LifecycleActor], "testy")
  //  testy ! math.Pi
  //  Thread.sleep(1000)
  //  testy ! 7
  //  Thread.sleep(1000)
  //  testy ! "hi there!"
  //  Thread.sleep(1000)
  testy ! Nil
  //  Thread.sleep(1000)
  testy ! "sorry about that"
  //  Thread.sleep(1000)
  system.stop(testy)
  Thread.sleep(1000)
  system.terminate()
}


class StringPrinter extends Actor {
  def receive: Receive = {
    case msg => println(s"child got message '$msg'")
  }

  override def preStart(): Unit = println(s"child about to start.")

  override def postStop(): Unit = println(s"child just stopped.")

}


class LifecycleActor extends Actor {
  var child: ActorRef = _

  def receive: Receive = {
    case num: Double => println(s"got a double - $num")
    case num: Int => println(s"got an integer - $num")
    case lst: List[_] => println(s"list - ${lst.head}, ...")
    case txt: String => child ! txt
  }

  override def preStart(): Unit = {
    println("about to start")
    child = context.actorOf(Props[StringPrinter], "kiddo")
  }

  override def preRestart(reason: Throwable, msg: Option[Any]): Unit = {
    println(s"about to restart because of $reason, during message $msg")
    super.preRestart(reason, msg)
  }

  override def postRestart(reason: Throwable): Unit = {
    println(s"just restarted due to $reason")
    super.postRestart(reason)
  }

  override def postStop(): Unit = println("just stopped")
}
