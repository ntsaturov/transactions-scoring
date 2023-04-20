import sbt.*

object Dependencies {
  object version {
    val akka = "2.6.18"
    val akkaKafka = "4.0.2"
    val kamonVersion = "2.4.7"
    val kamonPrometheusVersion = "2.4.7"
  }

  val commons = {
    val akkaCore = List(
      "com.typesafe.akka" %% "akka-stream-kafka" % version.akkaKafka,
      "com.typesafe.akka" %% "akka-stream" % version.akka
    )

    val kamonCore = "io.kamon" %% "kamon-core" % version.kamonVersion
    val kamonPrometheus = "io.kamon" %% "kamon-prometheus" % version.kamonPrometheusVersion

    List(kamonCore, kamonPrometheus) ++ akkaCore
  }
}
