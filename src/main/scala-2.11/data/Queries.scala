package data

import slick.driver.H2Driver.api._
import scala.concurrent.ExecutionContext.Implicits.global

object Queries {

  def users: DBIO[Vector[Tables.User]] =
    sql"select * from users".as[Tables.User]

}
