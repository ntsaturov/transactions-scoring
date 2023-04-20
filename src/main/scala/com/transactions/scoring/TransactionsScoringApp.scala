package com.transactions.scoring

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.RestartSettings
import akka.stream.scaladsl.{Flow, RestartSource, RetryFlow, Sink, Source}
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer

import java.util.concurrent.{Executors, ThreadFactory}
import scala.concurrent.duration.{FiniteDuration, SECONDS}
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Success, Failure}

object TransactionsScoringApp {
  lazy private val config = ConfigFactory.load()

  def threadFactory(name: String, daemon: Boolean = false): ThreadFactory =
    new ThreadFactory {
      val defaultFactory = Executors.defaultThreadFactory()

      override def newThread(r: Runnable): Thread = {
        val thread = defaultFactory.newThread(r)
        thread.setName(name)
        thread.setDaemon(daemon)
        thread
      }
    }
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem()
    implicit val ec: ExecutionContextExecutor = system.dispatcher

    flow()
      .onComplete { result =>
        result match {
          case Success(_) =>
          case Failure(e) => e.printStackTrace()
        }
        synchronized {
          notifyAll
        }
      }

    synchronized {
      wait
    }
  }

  private def flow()(implicit system: ActorSystem, ec: ExecutionContextExecutor) = {
    val consumerConfig = config.getConfig("akka.kafka.consumer")
    val bootstrapServers = config.getString("bootstrapServers")
    val topic = config.getString("topic")

    val consumerSettings =
      ConsumerSettings(consumerConfig, new StringDeserializer, new StringDeserializer)
        .withBootstrapServers(bootstrapServers)
        .withGroupId("group1")
        .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    def business(key: String, value: String) = {
      Future {123}
    }

    val consumer = Consumer
      .plainSource(consumerSettings, Subscriptions.topics(topic))
      .idleTimeout(FiniteDuration(10, SECONDS))
      .mapAsync(1)(record => {

        println(record.key())
        println(record.value())
        business(record.key, record.value())
      }).run()


    val settings: RestartSettings = RestartSettings(
      minBackoff = FiniteDuration(10, SECONDS),
      maxBackoff = FiniteDuration(10, SECONDS),
      randomFactor = 0,
    ).withMaxRestarts(Int.MaxValue, FiniteDuration(10, SECONDS))

    val source: Future[Done] = RestartSource
      .withBackoff(settings) { () => {
        Source
          .repeat()
          .via(
            RetryFlow.withBackoff(
              minBackoff = FiniteDuration(10, SECONDS),
              maxBackoff = FiniteDuration(10, SECONDS),
              randomFactor = 0,
              maxRetries = Int.MaxValue,
              Flow[Unit].mapAsync(1)(_ => consumer),
            )(decideRetry = {
              case result =>
                None
              case _ =>
                Some(())
            })
          )
        }
      }
      .runWith(Sink.ignore)
    source
  }
}
