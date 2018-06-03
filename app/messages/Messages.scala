package messages

import org.joda.time.DateTime

/**
  * Created by stela on 03.06.18.
  */
trait Command{
  val phoneNumber: String
}

trait Event{
  val timestamp: DateTime
}

case class RegisterUser(phoneNumber: String,userName:String) extends Command

case class UserRegistered(phoneNumber: String,
                         username: String,
                          timestamp: DateTime = DateTime.now
                         ) extends Event

case class InvalidCommand(reason: String)

case class SubscribeMentions(phoneNumber: String) extends Command