package tokens

import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json.Json

case class SignUpToken(tokenID: UUID, mailAddress: String,
                         expirationTime: DateTime
                        )

object SignUpToken {
  implicit val SignUpTokenJsonFormat = Json.format[SignUpToken]

}