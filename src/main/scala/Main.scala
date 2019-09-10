import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.alpakka.kinesis.ShardSettings
import akka.stream.alpakka.kinesis.scaladsl.KinesisSource
import akka.stream.scaladsl.Source
import com.amazonaws.services.kinesis.AmazonKinesisAsyncClientBuilder
import com.amazonaws.services.kinesis.model.ShardIteratorType

import scala.concurrent.duration._

object Main extends App {
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: Materializer = ActorMaterializer()

  implicit val amazonKinesisAsync: com.amazonaws.services.kinesis.AmazonKinesisAsync =
    AmazonKinesisAsyncClientBuilder.defaultClient()

  val settings =
    ShardSettings(streamName = ???, shardId = ???) //todo
      .withRefreshInterval(1.second)
      .withLimit(500)
      .withShardIteratorType(ShardIteratorType.TRIM_HORIZON)

  val source: Source[com.amazonaws.services.kinesis.model.Record, NotUsed] =
    KinesisSource.basic(settings, amazonKinesisAsync)

  system.registerOnTermination(amazonKinesisAsync.shutdown())

}