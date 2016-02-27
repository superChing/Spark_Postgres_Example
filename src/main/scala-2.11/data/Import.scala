package data

import slick.driver.H2Driver.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * import data into Relational DB
  */
object Import extends App {
  val db = Database.forConfig("h2mem1")

  import Tables._

  val importData: DBIO[Int] =
    createOccupation >> createUser >>
      createGenra >> createMovie >> createMovieGenra >>
      createRatings >>
      insertOccupations(Data.occupationRow) >> insertUsers(Data.users) >>
      insertGenras(Data.genraRows) >> insertMovies(Data.movies) >> insertMovieGenra(Data.movies) >>
      insertRating(Data.rating) //.map(n => println(s"number of inserts : $n"))

  val query: DBIO[Unit] =
    Queries.users.map { users => println(users.take(5)) }

  try {
    Await.result(db.run(importData >> query), 1.hour)
  } finally db.close
}

