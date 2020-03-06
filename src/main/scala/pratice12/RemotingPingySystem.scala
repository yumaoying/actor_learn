package pratice12

import akka.actor.Props

//相当于客户端，在pong关闭前，有15秒的操作时间
object RemotingPingySystem extends App {
  val system = remotingSystem("PingyDimension", 24567)
  val runner = system.actorOf(Props[Runner], "runner")
  runner ! "start"
  Thread.sleep(5000)
  system.terminate()
}