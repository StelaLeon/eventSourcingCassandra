package actors

import java.net.InetSocketAddress

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, Props}
import akka.io.{IO, Tcp}
import akka.io.Tcp._


/**
  * Created by stela on 03.06.18.
  */
class SMSServer extends Actor with ActorLogging{
  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", 6666))

  override def receive: Receive = {
    case Bound(localAddress) =>
      log.info("SMS server listening on {}", localAddress)
    case CommandFailed(_:Bind)=>
      context stop self
    case Connected(remote,local)=>
      val connection = sender()
      val handler = context.actorOf(Props(classOf[SMSHandler], connection))

      connection ! Register(handler)
  }
}
