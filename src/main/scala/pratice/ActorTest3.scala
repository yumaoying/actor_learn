package pratice

import scala.actors._
import scala.collection.mutable.ListBuffer
import scala.io.Source

/**
  * Created by yumaoying on 2019/7/25.
  * 使用Scala Actor完成文件中的单词出现频率统计
  * 需求:用actor并发编程写一个单机版的WordCount，将多个文件作为输入，计算完成后将多个任务汇总，得到最终的结果。
  *
  */
case class SubmitTask(f: String)

case object StopTask

object ActorTest3 extends App {
  //思路：
  //  1.通过loop +react 方式去不断的接受消息
  //  2.利用case class样例类去匹配对应的操作，其中scala中提供了文件读取的接口Source,通过调用其fromFile方法去获取文件内容
  //  3.将每个文件的单词数量进行局部汇总，存放在一个ListBuffer中
  //  4.最后将ListBuffer中的结果进行全局汇总。
  val replys = new ListBuffer[Future[Any]]
  val result = new ListBuffer[Map[String, Int]]
  //要处理的文件
  val files = Array("E:\\big01\\b0.txt", "E:\\big01\\b1.txt")
  val actor = new WordsCountActor
  actor.start()
  for (f <- files) {
    //异步处理，有返回结果
    val reply = actor !! SubmitTask(f)
    replys += reply
  }

  //对多个文件的汇总结果进行汇总
  while (replys.nonEmpty) {
    //判断结果是否哦可取
    val done = replys.filter(_.isSet)
    for (res <- done) {
      result += res.apply().asInstanceOf[Map[String, Int]]
      replys -= res
    }
    Thread.sleep(5000)
  }
  //对最后的结果进行汇总:获取结果按key进行分组，然后按值进行叠加(从0开始进行叠加)
  val res = result.flatten.groupBy(_._1).mapValues(_.foldLeft(0)(_ + _._2))
  //打印最后的结果
  println(res)
  //最后，退出
  actor ! StopTask
}

/**
  * 统计一个文本中单词出现的频率
  *
  */
class WordsCountActor extends Actor {

  override def act(): Unit = {
    loop {
      react {
        case SubmitTask(f) =>
          //把文件中的一行内容作为一个元素存入list
          val lines = Source.fromFile(f).getLines().toList
          //将文件中的每个单词作为一个元素存如list
          val words = lines.flatMap(_.split(" "))
          //得到一个一个map，当前文本的单词，以及响应单词出现的次数
          val result = words.map((_, 1)).groupBy(_._1).mapValues(_.size)
          println(result)
          sender ! result
        case StopTask =>
          //退出
          exit()
      }
    }
  }
}