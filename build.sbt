name := "hydroapp"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

val nscalatime = "com.github.nscala-time" %% "nscala-time" % "0.4.2"
val slick = "com.typesafe.play" %% "play-slick" % "0.6.0.1" 

libraryDependencies ++= Seq(
  jdbc,
  cache,
  nscalatime,
  slick,
  ws
)

includeFilter in (Assets, LessKeys.less) := "*.less"