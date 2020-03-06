package pratice10

import akka.actor.{ActorSystem, Props}
import pratice10.CommunicatingAsk.Master

object CommunicatingPoisonPill extends App {
  val ourSystem = ActorSystem("communicatingAskTest")
  val masta = ourSystem.actorOf(Props[Master], "masta")
  masta ! akka.actor.PoisonPill
  Thread.sleep(1000)
  ourSystem.terminate()
}