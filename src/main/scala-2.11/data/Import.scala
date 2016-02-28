package data

import slick.driver.H2Driver.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * import data into Relational DB
  */
object Import extends App {
  val db = Database.forConfig("db")

  import Tables._

  val importData: DBIO[Int] =
    createOccupation >> createUser >>
      createGenra >> createMovie >> createMovieGenra >>
      createRatings >>
      insertOccupations(Data.occupationRows) >> insertUsers(Data.users) >>
      insertGenras(Data.genreRows) >> insertMovies(Data.movies) >> insertMovieGenra(Data.movies) >>
      insertRating(Data.rating) //.map(n => println(s"number of inserts : $n"))

  val query: DBIO[Unit] =
    Queries.userSize.map(n => println(s"user size: $n")) >>
      Queries.movieSize.map(n => println(s"movie size: $n")) >>
      Queries.ratingSize.map(n => println(s"rating size: $n"))

  try {
    Await.result(db.run(importData >> query), 1.hour)
  } finally db.close
}

