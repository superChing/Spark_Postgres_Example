package compute

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}


object Rank {
  def apply(implicit sqlCtx: SQLContext) = {
    import org.apache.spark.sql.functions._
    import sqlCtx.implicits._

    println("loading data from DB.")
    val rating = Load("rating")
    val movie = Load("movie")
    val user = Load("users")

    println("computing movie-rate ranking.")
    val df = rating
      .join(movie, $"item_id" === $"id", "inner").drop($"id")
      .join(user, $"user_id" === $"id", "inner").drop($"id")

    df.groupBy($"item_id", $"title").agg(count("*"), avg($"rating"))
      .orderBy($"count(1)".desc)
      .drop($"item_id")
      .limit(20)
      .show()
  }
}

object Run extends App {
  val sparkConf = new SparkConf().setAppName("useJDBC")
  //.setMaster("local")
  val sc = new SparkContext(sparkConf)
  val sqlCtx = new SQLContext(sc)
  sqlCtx.setConf("spark.sql.shuffle.partitions", "4")

  Rank(sqlCtx)

  sc.stop()
}

