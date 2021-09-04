package io.github.ilinandrii

import io.circe.generic.semiauto.deriveCodec
import io.circe.{Codec, Decoder, Encoder}
import sttp.tapir.Schema
import zio.prelude._

case class User(name: User.Name, role: UserRole)

object User {

  object Name extends Newtype[String]
  type Name = Name.Type

  implicit lazy val nameSchema: Schema[Name] = Schema.schemaForString.as[Name]
  implicit lazy val nameCodec: Codec[Name] = Codec.from(
    decodeA = Decoder.decodeString.map(Name.wrap),
    encodeA = Encoder.encodeString.contramap(Name.unwrap)
  )

  implicit lazy val userSchema: Schema[User] = Schema.derived
  implicit lazy val userCodec: Codec[User]   = deriveCodec
}
