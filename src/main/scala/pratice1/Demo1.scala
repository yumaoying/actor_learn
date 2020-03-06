package pratice1

import akka.actor.{ActorSystem, Props}

import scala.io.StdIn

/** *
  *
  */
object Demo1 extends App {
  //1.新建一个ActorSystem,创建线程池对象system，用来创建actor的对象的
  //HelloSystem是线程池的名称
  val system = ActorSystem("HelloSystem")
  // 2.通过actorOf方法来创建一个actor，通过name参数设置Actor实例的唯一名字，当没有明确设置时，Actor会自动为新建的Actor实例赋予具有唯一性的名字
  // 3.配置对象，Actor配置对象中含有Actor类相关的信息：构造器参数，缓存区和分配器的实现代码,
  //   在Akka系统中，Actor配置对象是由Props类所代表的。Props对象封装了创建Actor实例所需的所有信息，
  //   而且该对象能够被序列化并且能够通过网络传输
  //apply[T <: Actor: ClassTag](creator: ⇒ T) 在创建HelloACtor实例时接受代码块,Props(new HelloACtor)
  //apply(clazz: Class[_], args: Any*)-通过Class对象和一系列构造参数创建Actor实例 Props(classOf[HelloACtor]

  val helloACtorRef = system.actorOf(Props(new HelloACtor), name = "helloActor")
  //  val helloACtorRef = system.actorOf(Props(classOf[HelloACtor]), name = "helloActor")
  var flag = true
  while (flag) {
    println("输入你想发送的消息:")
    val consoleLine = StdIn.readLine()
    helloACtorRef ! consoleLine
    if (consoleLine.equals("Stop")) {
      flag = false
      println("程序即将结束")
    }
  }
  //为了不让while的运行速度在receive方法之上，我们可以让他休眠0.1秒
  Thread.sleep(1000)
}


