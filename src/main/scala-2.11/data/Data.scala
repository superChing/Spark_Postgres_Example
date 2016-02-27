package data

import data.Tables.User

object Data {
  val occup: List[String] = io.Source.fromFile("data/ml-100k/u.occupation").getLines.toList
  val occupationRow: List[(Int, String)] = occup.zipWithIndex.map { case (a, b) => (b, a) }
  val users: Iterator[User] = io.Source.fromFile("data/ml-100k/u.user").getLines.map(_.split("\\|").toSeq).map {
    case Seq(a, b, c, d, e) => Tables.User(a.toInt, b.toInt, c, occup.indexOf(d), e)
  }

}
