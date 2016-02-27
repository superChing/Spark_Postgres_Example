package data

import slick.driver.H2Driver.api._

import scala.concurrent.ExecutionContext.Implicits.global

object Tables {

  import slick.jdbc.GetResult

  implicit val getUserResult = GetResult(r => User(r.nextInt, r.nextInt, r.nextString, r.nextInt, r.nextString))

  def createOccupations: DBIO[Int] =
    sqlu"""
       CREATE TABLE occupations (
         id integer NOT NULL,
         name varchar(255),
         PRIMARY KEY (id)
       )
        """

  def createUsers: DBIO[Int] =
    sqlu"""
       CREATE TABLE users (
         id integer NOT NULL,
         age integer,
         gender char(1),
         occupation_id integer,
         zip_code varchar(255),
         PRIMARY KEY (id)
       )
        """

  def insertOccupations(occupations: Iterator[(Int, String)]): DBIO[Int] = {
    DBIO.sequence(
      occupations.map { occup => sqlu"insert into occupations values (${occup._1},${occup._2})" }
    ).map(_.sum)
  }

  def insertUsers(users: Iterator[User]): DBIO[Int] = {
    def insert(u: User): DBIO[Int] =
      sqlu"insert into users values (${u.id}, ${u.age}, ${u.gender}, ${u.occupation_id}, ${u.zip_code})"

    DBIO.sequence(users.map(insert)).map(_.sum)
  }

  case class User(id: Int, age: Int, gender: String, occupation_id: Int, zip_code: String)


}