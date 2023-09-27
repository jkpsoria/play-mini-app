package models.domain

import java.util.UUID

case class Comment(id: UUID, comment: String, postID: UUID)

object Comment {
    val tupled = (apply: (UUID, String, UUID) => Comment).tupled
    def apply(comment: String, imageID: UUID, postID: UUID): Comment = apply(UUID.randomUUID(), comment, postID)
}
