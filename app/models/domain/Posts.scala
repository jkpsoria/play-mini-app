package models.domain

import java.util.UUID


case class Post(postID: UUID, description: String, userID: UUID)

object Post {
    val tupled = (apply: (UUID, String, UUID) => Post).tupled
    def apply(description: String, userID: UUID): Post = apply (UUID.randomUUID(), description, userID)
}