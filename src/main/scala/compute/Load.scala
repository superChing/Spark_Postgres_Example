package compute

import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.SQLContext


object DBConfig {
  private val config = ConfigFactory.load()
  private val dbConfig = config.getConfig("db")
  val url = dbConfig.getString("url")
  val driver = dbConfig.getString("driver")
}

object Load {
  println(s"connecting to ${DBConfig.url}")

  def apply(table: String)(implicit sqlContext: SQLContext) = sqlContext.read
    .format("jdbc")
    .options(Map(
      "url" -> DBConfig.url,
      "driver" -> DBConfig.driver,
      "dbtable" -> table))
    .load
}