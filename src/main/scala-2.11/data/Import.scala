package data

import slick.driver.H2Driver.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object Import extends App {
  val db = Database.forConfig("h2mem1")

  import Tables._

  val importData: DBIO[Int] = createOccupations >> createUsers >>
    insertOccupations(Data.occupationRow.toIterator) >> insertUsers(Data.users)

  val dbAction: DBIO[Unit] = importData.map(n => println(s"number of inserts : $n")) >>
    Queries.users.map { users => println(users.take(5)) }

  try {
    Await.result(db.run(dbAction), 1.hour)
  } finally db.close
}

