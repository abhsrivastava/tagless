import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
  lazy val cats = "org.typelevel" %% "cats-core" % "1.2.0"
  lazy val monix = "io.monix" %% "monix" % "2.3.3"
  lazy val monixCats = "io.monix" %% "monix-cats" % "2.3.3"
}
