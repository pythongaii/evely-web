name := "Play2-mongodb-silhouette-activator-template"

version := "1.0"

lazy val `mechanics` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

val reactiveMongoVersion = "0.11.14"

val playSilhouetteVersion = "4.0.0"

val scalaGuiceVersion = "4.0.0"

val ficusVersion = "1.1.2"

libraryDependencies ++= Seq(cache, ws)

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % reactiveMongoVersion,
  "com.mohiva" %% "play-silhouette" % playSilhouetteVersion,
  "com.mohiva" %% "play-silhouette-password-bcrypt" % playSilhouetteVersion,
  "com.mohiva" %% "play-silhouette-persistence" % playSilhouetteVersion,
  "com.mohiva" %% "play-silhouette-crypto-jca" % "4.0.0",
  "com.mohiva" %% "play-silhouette-testkit" % playSilhouetteVersion % "test",
  "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.1" % "test",
  "org.mockito" % "mockito-all" % "1.10.19" % "test",
  "net.codingwell" %% "scala-guice" % scalaGuiceVersion,
  "net.ceedubs" %% "ficus" % ficusVersion
)

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"