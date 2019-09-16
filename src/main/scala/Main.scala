import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.{ActorMaterializer, IOResult, Materializer}
import org.mongodb.scala.MongoClient
import residenthomeassignment.api.EventRoute
import residenthomeassignment.data.DataMeasurement
import residenthomeassignment.streams.{FileSource, MongoStream}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.StdIn

object Main extends App {

  val path: String = args.headOption.getOrElse(throw new RuntimeException("You should provide filepath as an arg"))

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: Materializer = ActorMaterializer()

  val mongo = MongoClient("mongodb://localhost:27017")
  val db = mongo.getDatabase("MongoSourceSpec")

  val fromFile: Source[DataMeasurement, Future[IOResult]] = FileSource.createSource(path, DataMeasurement.apply)
  val mongoSink: Sink[DataMeasurement, Future[Done]] = MongoStream.createSink("measurements", db)

  fromFile.to(mongoSink).run()

  val route = EventRoute.createRoute(db, "measurements")

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())

  sys.addShutdownHook(system.terminate())
}
