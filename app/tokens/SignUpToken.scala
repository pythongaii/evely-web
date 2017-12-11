package tokens

import java.util.UUID

import org.joda.time.DateTime
import play.api.libs.json.Json

case class SignUpToken(tokenID: UUID,
                       mailAddress: String,
                       expirationTime: DateTime,
                       isSignUp: Boolean) {
  // 一時トークンの期限が切れているか確認
  def isExpired = expirationTime.isBeforeNow
}

object SignUpToken {
  implicit val SignUpTokenJsonFormat = Json.format[SignUpToken]

}