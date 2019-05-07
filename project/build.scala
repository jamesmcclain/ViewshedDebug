import sbt._
import scala.util.Properties

object Version {
  def either(environmentVariable: String, default: String): String = Properties.envOrElse(environmentVariable, default)

  lazy val hadoop  = either("SPARK_HADOOP_VERSION", "2.7.4")
  lazy val spark   = either("SPARK_VERSION", "2.3.3")
}
