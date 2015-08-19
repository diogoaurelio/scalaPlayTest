name := """play-scala-test"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "1.0.1",
  "com.typesafe.play" %% "play-slick-evolutions" % "1.0.1",
  //"com.h2database" % "h2" % "1.4.177",
  "org.postgresql" % "postgresql" % "9.4-1200-jdbc41",
  //"com.edulify" %% "play-hikaricp" % "2.0.6", //not needed, used by default
  "org.seleniumhq.selenium" % "selenium-java" % "2.42.2",
  "org.webjars" %% "webjars-play" % "2.4.0-1",
  "org.webjars" % "bootstrap" % "3.3.4",
  "org.webjars"    %    "bootswatch-cerulean"   % "3.3.1+2",
  "org.webjars"    %    "html5shiv"             % "3.7.0",
  "org.webjars"    %    "respond"               % "1.4.2",
  "com.typesafe.play" %% "play-mailer" % "3.0.1",
  ws,
  specs2 % Test
)


resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

resolvers += Resolver.url("Edulify Repository", url("https://edulify.github.io/modules/releases/"))(Resolver.ivyStylePatterns)

includeFilter in (Assets, LessKeys.less) := "*.less"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

