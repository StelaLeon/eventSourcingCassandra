package actors

import javax.inject.Inject

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, Props}
import com.google.inject.AbstractModule
import play.libs.akka.AkkaGuiceSupport



/**
  * Created by stela on 03.06.18.
  */
class SMSService @Inject() extends Actor with ActorLogging{

  override def preStart(): Unit = {
    context.actorOf(Props[SMSServer])
    context.actorOf(Props[CQRSCommandHandler])
  }

  override def receive: Receive = {
    case message => // TODO
  }
}

class SMSServiceModule extends AbstractModule with AkkaGuiceSupport {
  def configure(): Unit ={
    bindActor(classOf[SMSService],"sms")
  }
}