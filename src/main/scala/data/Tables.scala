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
       CREATE TABLE IF NOT EXISTS occupation (
         id integer NOT NULL,
         name varchar(255),
         PRIMARY KEY (id)
       )
        """

  def createUser: DBIO[Int] =
    sqlu"""
       CREATE TABLE IF NOT EXISTS users (
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
       CREATE TABLE IF NOT EXISTS genre (
         id integer NOT NULL,
         genre varchar(255),
         PRIMARY KEY (id)
       )
        """

  def createMovie: DBIO[Int] =
    sqlu"""
       CREATE TABLE IF NOT EXISTS movie (
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
       CREATE TABLE IF NOT EXISTS movie_genre (
         movie_id integer NOT NULL,
         genre_id integer NOT NULL
       )
        """

  def createRatings: DBIO[Int] =
    sqlu"""
       CREATE TABLE IF NOT EXISTS rating (
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
      sqlu"insert into users values (${u.id}, ${u.age}, ${u.gender}, ${u.occupation_id}, ${u.zip_code})"
    DBIO.sequence(users.map(insert)).map(_.sum)
  }

  def insertGenras(genras: TraversableOnce[(Int, String)]): DBIO[Int] = {
    def insert(genra: (Int, String)): DBIO[Int] = sqlu"insert into genre values (${genra._1},${genra._2})"
    DBIO.sequence(genras.map(insert)).map(_.sum)
  }

  def insertMovies(movies: TraversableOnce[Movie]): DBIO[Int] = {
    def insert(movie: Movie): DBIO[Int] =
      sqlu"insert into movie values (${movie.id}, ${movie.title}, ${movie.releaseDate}, ${movie.videoDate}, ${movie.url})"
    DBIO.sequence(movies.map(insert)).map(_.sum)
  }

  def insertMovieGenra(movies: TraversableOnce[Movie]): DBIO[Int] = {
    def insert(movie_id: Int, genre_id: Int): DBIO[Int] = sqlu"insert into movie_genre values (${movie_id},$genre_id)"
    DBIO.sequence(for {
      movie <- movies
      genra_id <- movie.genreIds
    } yield insert(movie.id, genra_id)).map(_.sum)
  }

  def insertRating(ratings: TraversableOnce[Rating]): DBIO[Int] = {
    def insert(rating: Rating): DBIO[Int] =
      sqlu"insert into rating values (${rating.user_id}, ${rating.item_id}, ${rating.rating}, ${rating.timestamp})"
    DBIO.sequence(ratings.map(insert)).map(_.sum)
  }


}