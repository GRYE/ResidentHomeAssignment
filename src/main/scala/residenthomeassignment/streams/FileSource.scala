package residenthomeassignment.streams

import java.nio.file.Paths

import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Framing, Source}
import akka.util.ByteString

import scala.concurrent.Future

object FileSource {

  def createSource[A](fileName: String, mapper: String => Either[Throwable, A], delimiter: String = "\n"): Source[A, Future[IOResult]] =
    FileIO.fromPath(Paths.get(fileName))
      .via(Framing.delimiter(ByteString(delimiter), 256, allowTruncation = true).map(_.utf8String))
      .map(mapper)
      .collect {
        case Right(value) => value
      }
}
