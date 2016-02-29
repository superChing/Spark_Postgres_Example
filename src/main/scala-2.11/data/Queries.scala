package data

import slick.driver.H2Driver.api._
import scala.concurrent.ExecutionContext.Implicits.global

object Queries {

  def userSize: DBIO[Vector[Int]] = sql"select COUNT(*) from users".as[Int]
  def movieSize: DBIO[Vector[Int]] = sql"select COUNT(*) from movie".as[Int]
  def ratingSize: DBIO[Vector[Int]] = sql"select COUNT(*) from rating".as[Int]
}
