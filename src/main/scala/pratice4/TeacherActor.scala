package pratice4

import akka.actor.{Actor, ActorLogging}

import scala.util.Random

/**
  * Created by yumaoying
  */
class TeacherActor extends Actor with ActorLogging {

  val quotes = List(
    "Moderation is for cowards",
    "Anything worth doing is worth overdoing",
    "The trouble is you think you have time",
    "You never gonna know if you never even try")

  override def receive: Receive = {
    //    　　1、匹配QuoteRequest；
    //    　　2、从quotes中随机取出一个quote；
    //    　　3、构造一个QuoteResponse；
    //    　　4、在控制台打印QuoteResponse
    case QuoteRequest =>
      //随机取出一条
      val quote = quotes(Random.nextInt(quotes.size))
      val quoteResponse = QuoteResponse(quote)
      log.info(quoteResponse.quoteString)
      //发送
      sender ! quoteResponse
  }
}
