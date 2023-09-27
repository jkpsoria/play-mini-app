package models.domain

import java.util.UUID


case class User(userID: UUID, username: String, password: String)

object User {
    val tupled = (apply: (UUID, String, String) => User).tupled
    def apply(username: String, password: String): User = apply (UUID.randomUUID(), username, password)
}