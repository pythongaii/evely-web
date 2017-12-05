package model.reviewable

import java.awt.Image
import java.util.Date

import model.user.RegisterdUser
import play.api.libs.json.Json


case class Comment(images: Option[List[String]], textContent: String,
                   createdAt: Date, author: RegisterdUser
              )


object Comment {
  implicit val CommentJsonFormat = Json.format[Comment]
}