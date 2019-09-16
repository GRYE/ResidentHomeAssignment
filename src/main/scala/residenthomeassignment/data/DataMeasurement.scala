package residenthomeassignment.data

import scala.util.Try

case class DataMeasurement(ts: Long, eventType: String, value: Double)

object DataMeasurement {

  def apply(s: String): Either[Throwable, DataMeasurement] = Try {
    val arr = s.split(",")
    DataMeasurement(arr(0).toLong, arr(1), arr(2).toDouble)
  }.toEither

}
