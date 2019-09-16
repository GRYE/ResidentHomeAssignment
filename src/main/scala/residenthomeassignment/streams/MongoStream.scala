package residenthomeassignment.streams

import akka.Done
import akka.stream.scaladsl.Sink
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.{MongoClient, MongoDatabase}
import residenthomeassignment.data.DataMeasurement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object MongoStream {

  def createSink(collName: String, db: MongoDatabase): Sink[DataMeasurement, Future[Done]] = {
    val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[DataMeasurement]), DEFAULT_CODEC_REGISTRY)
    val coll = db
      .getCollection[DataMeasurement](collName)
      .withCodecRegistry(codecRegistry)
    Sink.foreachAsync(4)(el => coll.insertOne(el).toFuture().map(_ => ()))
  }

}
