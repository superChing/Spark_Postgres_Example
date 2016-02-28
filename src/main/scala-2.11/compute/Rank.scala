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

  val df = Load.table("RATING")
    .join(Load.table("MOVIE"), $"ITEM_ID" === $"ID", "inner").drop($"ID")
    .join(Load.table("USER"), $"USER_ID" === $"ID", "inner").drop($"ID")

  df.groupBy($"ITEM_ID", $"TITLE").agg(count("*"),avg($"RATING"))
    .orderBy($"count(1)".desc)
    .drop($"ITEM_ID")
    .limit(10)
    .show()

  sc.stop()
}
