import sbtassembly.PathList

lazy val commonSettings = Seq(
  organization := "com.example",
  version := "0",
  scalaVersion := "2.11.12",
  test in assembly := {},
  scalacOptions ++= Seq(
    "-deprecation",
    "-unchecked",
    "-feature",
    "-language:implicitConversions",
    "-language:reflectiveCalls",
    "-language:higherKinds",
    "-language:postfixOps",
    "-language:existentials",
    "-language:experimental.macros",
    "-feature"),
  assemblyMergeStrategy in assembly := {
    case "log4j.properties" => MergeStrategy.first
    case "reference.conf" => MergeStrategy.concat
    case "application.conf" => MergeStrategy.concat
    case PathList("META-INF", xs @ _*) =>
      xs match {
        case ("MANIFEST.MF" :: Nil) => MergeStrategy.discard
        case ("services" :: _ :: Nil) =>
          MergeStrategy.concat
        case ("javax.media.jai.registryFile.jai" :: Nil) | ("registryFile.jai" :: Nil) | ("registryFile.jaiext" :: Nil) =>
          MergeStrategy.concat
        case (name :: Nil) => {
          if (name.endsWith(".RSA") || name.endsWith(".DSA") || name.endsWith(".SF"))
            MergeStrategy.discard
          else
            MergeStrategy.first
        }
        case _ => MergeStrategy.first
      }
    case _ => MergeStrategy.first
  },
  shellPrompt := { s => Project.extract(s).currentProject.id + " > " },
  resolvers  += Resolver.mavenLocal,
)

lazy val root = (project in file("."))
  .settings(commonSettings: _*)

lazy val vs = (project in file("vs"))
  .dependsOn(root)
  .settings(commonSettings: _*)
