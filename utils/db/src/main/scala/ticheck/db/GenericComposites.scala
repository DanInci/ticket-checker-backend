package ticheck.db

import java.sql.Timestamp

import doobie.util.meta.{LegacyInstantMetaInstance, LegacyLocalDateMetaInstance}

import scala.reflect.runtime.universe.TypeTag
import io.circe.Json
import io.circe.parser.parse
import org.postgresql.util.PGobject
import shapeless.tag.@@
import ticheck.InconsistentState
import ticheck.json._
import ticheck.effect._

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
trait GenericComposites extends LegacyLocalDateMetaInstance with LegacyInstantMetaInstance {

  implicit val timestampMeta: Meta[Timestamp] = doobie.implicits.javasql.TimestampMeta

  implicit def phantomLongMeta[Tag](implicit tt: TypeTag[Long @@ Tag]): Meta[Long @@ Tag] =
    Meta.LongMeta.timap(v => shapeless.tag[Tag](v))(identity)

  implicit def phantomFloatMeta[Tag](implicit tt: TypeTag[Float @@ Tag]): Meta[Float @@ Tag] =
    Meta.FloatMeta.timap(v => shapeless.tag[Tag](v))(identity)

  implicit def phantomDoubleMeta[Tag](implicit tt: TypeTag[Double @@ Tag]): Meta[Double @@ Tag] =
    Meta.DoubleMeta.timap(v => shapeless.tag[Tag](v))(identity)

  implicit def phantomBooleanMeta[Tag](implicit tt: TypeTag[Boolean @@ Tag]): Meta[Boolean @@ Tag] =
    Meta.BooleanMeta.timap(v => shapeless.tag[Tag](v))(identity)

  implicit def phantomIntMeta[Tag](implicit tt: TypeTag[Int @@ Tag]): Meta[Int @@ Tag] =
    Meta.IntMeta.timap(v => shapeless.tag[Tag](v))(identity)

  implicit def phantomStringMeta[Tag]: Meta[String @@ Tag] =
    Meta.StringMeta.asInstanceOf[Meta[String @@ Tag]]

  implicit def jsonMeta[A](implicit codec: Codec[A]): Meta[A] =
    Meta.Advanced
      .other[PGobject]("jsonb")
      .imap(a => {
        val json = parse(a.getValue).getOrElse(Json.Null)
        codec
          .decodeJson(json)
          .leftMap { e =>
            InconsistentState(s"Failed to read JSON from DB. THIS IS A BUG!!! '$e'", "jsonMeta[A]", Option(e)): Throwable
          }
          .unsafeGet()
      })(
        (a: A) => {
          val o = new PGobject
          o.setType("jsonb")
          o.setValue(codec(a).noSpaces)
          o
        },
      )

}
