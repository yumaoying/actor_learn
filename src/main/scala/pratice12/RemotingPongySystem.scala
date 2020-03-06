package pratice12

import akka.actor.Props
import pratice10.Pongy

//2.创建一个应用程序，使用24321实例化名文PongyDimension的Actor系统，运行15秒后关闭
//启动该程序后，只能在限定的一小段时间内，启动另一个程序
object RemotingPongySystem extends App {
  val system = remotingSystem("PongyDimension", 24321)
  val pongy = system.actorOf(Props[Pongy], "pongy")
  Thread.sleep(15000)
  system.terminate()
}