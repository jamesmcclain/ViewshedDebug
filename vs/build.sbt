name := "vs"
libraryDependencies ++= Seq(
  "org.locationtech.geotrellis" %% "geotrellis-raster"    % "3.0.1-SNAPSHOT",
  "org.locationtech.geotrellis" %% "geotrellis-spark"     % "3.0.1-SNAPSHOT",
  "org.apache.hadoop"            % "hadoop-client"        % Version.hadoop % "provided",
  "org.apache.spark"            %% "spark-core"           % Version.spark  % "provided"
)

fork in Test := false
parallelExecution in Test := false
