package compute

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

object Rank extends App {
  val sparkConf = new SparkConf().setAppName("fromJDBC").setMaster("local")
  val sc = new SparkContext(sparkConf)
  implicit val sqlCtx = new SQLContext(sc)
  sqlCtx.setConf("spark.sql.shuffle.partitions","4")

  import sqlCtx.implicits._
  import org.apache.spark.sql.functions._

  val df = Load.table("rating")
    .join(Load.table("movie"), $"item_id" === $"id", "inner").drop($"id")
    .join(Load.table("users"), $"user_id" === $"id", "inner").drop($"id")

  df.groupBy($"item_id", $"title").agg(count("*"),avg($"rating"))
    .orderBy($"count(1)".desc)
    .drop($"item_id")
    .limit(10)
    .show()

  sc.stop()
}
