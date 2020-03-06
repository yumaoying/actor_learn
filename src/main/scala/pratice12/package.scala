import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

package object pratice12 {

  //使用名为RemoteActorRefProvider的自定义ActorRef工厂对象，它可以创建用于实现远程通信的Actor对象引用
  //Actor系统通过TCP网络层和指定的tcp端口，使用Netty网络库
  def remotingConfig(port: Int) = ConfigFactory.parseString(
    s"""
akka {
  actor.provider = "akka.remote.RemoteActorRefProvider"
  remote {
    enabled-transports = ["akka.remote.netty.tcp"]
    netty.tcp {
      hostname = "127.0.0.1"
      port = $port
    }
  }
}
  """)

  //1.在两个actotr进行通讯前，需要创建一个能够使远程Actor进行通讯的Actor系统
  //创建哪一个自定义的Actor系统配置字符串，这个字符串可以用于配置一系列Actor属性
  def remotingSystem(name: String, port: Int) = ActorSystem(name, remotingConfig(port))
}
