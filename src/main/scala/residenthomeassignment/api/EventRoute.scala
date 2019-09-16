package residenthomeassignment.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.model.Filters._
import residenthomeassignment.data.DataMeasurement
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object EventRoute {

  def createRoute(db: MongoDatabase, collectionName: String): Route = {
    val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[DataMeasurement]), DEFAULT_CODEC_REGISTRY)
    val coll = db.getCollection(collectionName).withCodecRegistry(codecRegistry)

    get {
      path(Segment / "average") { eventType =>
        parameters("from".as[Long], "to".as[Long]) { (from, to) =>
          val filter = and(equal("eventType", eventType), gte("ts", from), lte("ts", to))

          onSuccess(coll.find[DataMeasurement](filter).toFuture()) { values =>
            val resp =
              if (values.isEmpty) Response(eventType, 0.0, 0)
              else Response(eventType, values.map(_.value).sum / values.size, values.size)
              complete(resp)
          }
        }
      }
    }
  }

}

case class Response(`type`: String, value: Double, processedCount: Int)
object Response extends SprayJsonSupport with DefaultJsonProtocol {
  implicit def cardJsonFormat: RootJsonFormat[Response] = jsonFormat3(Response.apply)
}
