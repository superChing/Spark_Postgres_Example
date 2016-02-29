package data

import scala.io

/**
  * data model ,
  * load data source
  */
object Data {

  val occup: Seq[String] = io.Source.fromFile("data/ml-100k/u.occupation").getLines.toSeq
  val occupationRows: Seq[(Int, String)] = occup.zipWithIndex.map { case (a, b) => (b, a) }
  val genreRows: Seq[(Int, String)] = io.Source.fromFile("data/ml-100k/u.genre").getLines
    .map(_.split("\\|").toSeq)
    .flatMap {
      case Seq(a, b) => Option(b.toInt, a)
      case Seq("") => None
    }.toSeq

  // the following are iterators
  def users: Iterator[User] = io.Source.fromFile("data/ml-100k/u.user").getLines
    .map(_.split("\\|").toSeq)
    .map {
      case Seq(a, b, c, d, e) => User(a.toInt, b.toInt, c, occup.indexOf(d), e)
    }

  def movies: Iterator[Movie] = io.Source.fromFile("data/ml-100k/u.item")("Latin1").getLines
    .map(_.split("\\|").toSeq)
    .map {
      case Seq(x1, x2, x3, x4, x5, rest@_*) =>
        val genra = rest.map(_.toInt).zipWithIndex.collect { case (1, i) => i }
        Movie(x1.toInt, x2, x3, x4, x5, genra)
    }

  def rating: Iterator[Rating] = io.Source.fromFile("data/ml-100k/u.data").getLines
    .map(_.split("\t").toSeq)
    .map {
      case Seq(x1, x2, x3, x4) => Rating(x1.toInt, x2.toInt, x3.toInt, x4.toLong)
    }

  // row definitions
  case class Rating(user_id: Int, item_id: Int, rating: Int, timestamp: Long)

  case class Movie(id: Int, title: String, releaseDate: String, videoDate: String, url: String, genreIds: Seq[Int])

  case class User(id: Int, age: Int, gender: String, occupation_id: Int, zip_code: String)

}
