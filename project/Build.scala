import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "hydroapp"
  val appVersion = "1.0-SNAPSHOT"

  val nscalatime = "com.github.nscala-time" %% "nscala-time" % "0.4.2"
  val slick = "com.typesafe.play" %% "play-slick" % "0.5.0.8" 

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    cache,
    nscalatime,
    slick)

  val main = play.Project(appName, appVersion, appDependencies).settings( // Add your own project settings here      
  )

}
