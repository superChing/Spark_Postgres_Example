package data

import slick.driver.H2Driver.api._
import slick.jdbc.GetResult

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * table creation and insertion helper functions
  */
object Tables {

  import Data._

  implicit val getUserResult = GetResult(r => User(r.nextInt, r.nextInt, r.nextString, r.nextInt, r.nextString))

  def createOccupation: DBIO[Int] =
    sqlu"""
       CREATE TABLE occupation (
         id integer NOT NULL,
         name varchar(255),
         PRIMARY KEY (id)
       )
        """

  def createUser: DBIO[Int] =
    sqlu"""
       CREATE TABLE user (
         id integer NOT NULL,
         age integer,
         gender char(1),
         occupation_id integer,
         zip_code varchar(255),
         PRIMARY KEY (id)
       )
        """

  def createGenra: DBIO[Int] =
    sqlu"""
       CREATE TABLE genra(
         id integer NOT NULL,
         genra varchar(255),
         PRIMARY KEY (id)
       )
        """

  def createMovie: DBIO[Int] =
    sqlu"""
       CREATE TABLE movie (
         id integer NOT NULL,
         title varchar(255),
         release_date varchar(255),
         video_date varchar(255),
         url varchar(255),
         PRIMARY KEY (id)
       )
        """

  def createMovieGenra: DBIO[Int] =
    sqlu"""
       CREATE TABLE movie_genra (
         movie_id integer NOT NULL,
         genra_id integer NOT NULL,
       )
        """

  def createRatings: DBIO[Int] =
    sqlu"""
       CREATE TABLE rating (
         user_id integer NOT NULL,
         item_id integer NOT NULL,
         rating integer,
         timestamp varchar(255),
         PRIMARY KEY (user_id,item_id)
       )
        """

  def insertOccupations(occupations: TraversableOnce[(Int, String)]): DBIO[Int] = {
    def insert(occup: (Int, String)) = sqlu"insert into occupation values (${occup._1},${occup._2})"
    DBIO.sequence(occupations.map(insert)).map(_.sum)
  }

  def insertUsers(users: TraversableOnce[User]): DBIO[Int] = {
    def insert(u: User): DBIO[Int] =
      sqlu"insert into user values (${u.id}, ${u.age}, ${u.gender}, ${u.occupation_id}, ${u.zip_code})"
    DBIO.sequence(users.map(insert)).map(_.sum)
  }

  def insertGenras(genras: TraversableOnce[(Int, String)]): DBIO[Int] = {
    def insert(genra: (Int, String)): DBIO[Int] = sqlu"insert into genra values (${genra._1},${genra._2})"
    DBIO.sequence(genras.map(insert)).map(_.sum)
  }

  def insertMovies(movies: TraversableOnce[Movie]): DBIO[Int] = {
    def insert(movie: Movie): DBIO[Int] =
      sqlu"insert into movie values (${movie.id}, ${movie.title}, ${movie.releaseDate}, ${movie.videoDate}, ${movie.url})"
    DBIO.sequence(movies.map(insert)).map(_.sum)
  }

  def insertMovieGenra(movies: TraversableOnce[Movie]): DBIO[Int] = {
    def insert(movie_id: Int, genra_id: Int): DBIO[Int] = sqlu"insert into movie_genra values (${movie_id},$genra_id)"
    DBIO.sequence(for {
      movie <- movies
      genra_id <- movie.genraIds
    } yield insert(movie.id, genra_id)).map(_.sum)
  }

  def insertRating(ratings: TraversableOnce[Rating]): DBIO[Int] = {
    def insert(rating: Rating): DBIO[Int] =
      sqlu"insert into rating values (${rating.user_id}, ${rating.item_id}, ${rating.rating}, ${rating.timestamp})"
    DBIO.sequence(ratings.map(insert)).map(_.sum)
  }


}