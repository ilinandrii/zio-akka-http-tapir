package io.github.ilinandrii

import io.circe.Codec
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
import io.circe.generic.extras.{Configuration => CirceConfiguration}
import sttp.tapir.Schema
import sttp.tapir.generic.{Configuration => TapirConfiguration}

sealed trait UserRole

object UserRole {
  implicit val tapirConfiguration: TapirConfiguration = TapirConfiguration.default.withDiscriminator("type")
  implicit val circeConfiguration: CirceConfiguration = CirceConfiguration.default.withDiscriminator("type")

  implicit lazy val codec: Codec[UserRole]   = deriveConfiguredCodec
  implicit lazy val schema: Schema[UserRole] = Schema.derived

  case class Administrator(securityCode: Long) extends UserRole
  case class Regular(promoCode: Long)          extends UserRole
}
