package actors

import akka.actor.ActorLogging
import akka.actor._
import akka.persistence._
import messages._
import akka.persistence.{PersistentActor, RecoveryCompleted}

import scala.concurrent.duration._

/**
  * Created by stela on 03.06.18.
  */
class CQRSCommandHandler extends PersistentActor with ActorLogging{

  override def persistenceId: String = "CQRSCommandHandler"

  override def onPersistFailure(cause: Throwable,
                                event: Any,
                                seqNr: Long): Unit = {
    log.error(cause, "Failed to recover!")
  }

  override def receiveRecover: Receive  = {
    case RecoveryCompleted =>
      log.info("Recovery completed")
    case evt: Event =>
      handleEvent(evt)
  }

  override def receiveCommand: Receive = {
    case RegisterUser(phoneNumber, username) =>
      persist(UserRegistered(phoneNumber, username))(handleEvent)
    case command: Command =>
      context.child(command.phoneNumber).map { reference =>
        reference forward command
      } getOrElse{
        sender() ! "User Unknown"
      }
  }

  def handleEvent(event: Event): Unit =
    event match {
      case registered @ UserRegistered(phoneNumber, userName, _) =>
        context.actorOf(
          props = Props(
            classOf[ClientCommandHandler], phoneNumber, userName
          ),
          name = phoneNumber)
        if(recoveryFinished){
          sender() ! registered
        }
    }
}
