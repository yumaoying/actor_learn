package pratice4

import akka.actor.{Actor, ActorLogging, ActorRef}

/**
  * Created by yumaoying
  * 双向通讯
  */
class StudentActor(teacherActor: ActorRef) extends Actor with ActorLogging {

  override def receive: Receive = {
    case InitSignal =>
      teacherActor ! QuoteRequest
    case QuoteResponse(quoteString) =>
      log.info("Received QuoteResponse from Teacher")
      log.info(s"Printing from Student Actor $quoteString")
  }
}
