package data


/**
  * data model ,
  * load data source
  */
object Data {

  val occup: Seq[String] = io.Source.fromFile("data/ml-100k/u.occupation").getLines.toSeq
  val occupationRow: Seq[(Int, String)] = occup.zipWithIndex.map { case (a, b) => (b, a) }
  val genra = Seq("unknown", " Action", " Adventure", " Animation", "Children's", " Comedy", " Crime", " Documentary", " Drama", " Fantasy", "Film-Noir", " Horror", " Musical", " Mystery", " Romance", " Sci-Fi", "Thriller", " War", " Western")
  val genraRows: Seq[(Int, String)] = genra.zipWithIndex.map { case (a, b) => (b, a) }

  def users: Iterator[User] = io.Source.fromFile("data/ml-100k/u.user").getLines.map(_.split("\\|").toSeq).map {
    case Seq(a, b, c, d, e) => User(a.toInt, b.toInt, c, occup.indexOf(d), e)
  }

  def movies: Iterator[Movie] = io.Source.fromFile("data/ml-100k/u.item")("Latin1").getLines.map(_.split("\\|").toSeq).map {
    case Seq(x1, x2, x3, x4, x5, rest@_*) =>
      val genra = rest.map(_.toInt).zipWithIndex.collect { case (1, i) => i }
      Movie(x1.toInt, x2, x3, x4, x5, genra)
  }

  def rating: Iterator[Rating] = io.Source.fromFile("data/ml-100k/u.data").getLines.map(_.split("\t").toSeq).map {
    case Seq(x1, x2, x3, x4) => Rating(x1.toInt, x2.toInt, x3.toInt, x4.toLong)
  }

  case class Rating(user_id: Int, item_id: Int, rating: Int, timestamp: Long)

  case class Movie(id: Int, title: String, releaseDate: String, videoDate: String, url: String, genraIds: Seq[Int])

  case class User(id: Int, age: Int, gender: String, occupation_id: Int, zip_code: String)
}
