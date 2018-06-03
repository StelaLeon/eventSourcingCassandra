package actors

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.Tcp.{PeerClosed, Received, Write}
import akka.util.{ByteString, Timeout}
import messages.{InvalidCommand, RegisterUser, UserRegistered}

import scala.concurrent.duration._

/**
  * Created by stela on 03.06.18.
  */
class SMSHandler(connection: ActorRef) extends Actor with ActorLogging{
  implicit val timeout = Timeout(2.seconds)
  implicit val ec = context.dispatcher


  lazy val commandHandler = context
    .actorSelection("akka://application/user/sms/commandHandler")

  val MessagePattern = """[\+]([0-9]*) (.*)""".r
  val RegistrationPattern = """register (.*)""".r

  override def receive: Receive = {

    case Received(data) =>
      log.info("Received message: {}", data.utf8String)
      data.utf8String.trim match {
        case MessagePattern(number, message) =>
          message match {
            case RegistrationPattern(userName) =>
              commandHandler ! RegisterUser(number, userName)
            case other =>
              log.warning("Invalid message {}", other)
              sender() ! Write(ByteString("Invalid message format\n"))
          }
      }
    case registered: UserRegistered =>
      connection ! Write(ByteString("Registration successful\n"))

    case InvalidCommand(reason) =>
      connection ! Write(ByteString(reason + "\n"))

    case PeerClosed => context stop self
  }
}
