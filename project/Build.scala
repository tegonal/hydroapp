import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "hydroapp"
  val appVersion = "1.0-SNAPSHOT"

  val nscalatime = "com.github.nscala-time" %% "nscala-time" % "0.4.2"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    cache,
    nscalatime)

  val main = play.Project(appName, appVersion, appDependencies).settings( // Add your own project settings here      
  ).dependsOn(RootProject(uri("git://github.com/freekh/play-slick.git")))

}
