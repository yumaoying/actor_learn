package pratice

import scala.actors.Actor

/**
  * Created by yumaoying on 2019/8/25.
  * scala actor的基本操作-单向通讯
  * 和java中的Runnable Thread几乎一致
  * 第一步：编写一个类，扩展特质trait Actor（scala 的actor）
  * 第二步：复写其中的act方法
  * 第三步：创建该actor的对象，调用该对象的start()方法，启动该线程
  * 第四步：通过scala的操作符"!"，发送消息
  */
object Demo extends App {
  //构建Actor对象，于thread的创建和启动类似
  //调用start方法启动
  val actor = new FirstActor()
  actor.start()
  //发送消息
  for (i <- 1 to 3) {
    //注意：发送start消息和stop的消息是异步的，但是Actor接收到消息执行的过程是同步的按顺序执行
    actor ! "start"
    actor ! "stop"
  }
  println("==============")
}


//继承Actor类，相当于java中的Thread
class FirstActor extends Actor {

  //重写act方法，相当于java中的run方法
  override def act(): Unit = {
    //说明：在act()方法中加入了while (true) 循环，就可以不停的接收消息
    //act方法中通过receive方法接受消息并进行相应的处理
//    while (true) {
      receive {
        case "start" =>
          println("starting...")
          Thread.sleep(1000)
          println("start")
        case "stop" =>
          println("stoping...")
          Thread.sleep(1000)
          println("stop")
      }
//    }
    //    loop {
    //      //react方式会复用线程，避免频繁的线程创建、销毁和切换。比receive更高效
    //      //注意: react 如果要反复执行消息处理，react外层要用loop，不能用while
    //      react {
    //        case "start" =>
    //          println("starting...")
    //          Thread.sleep(1000)
    //          println("start")
    //        case "stop" =>
    //          println("stoping...")
    //          Thread.sleep(1000)
    //          println("stop")
    //      }
    //    }
  }
}