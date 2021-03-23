package com.ntny.web.middleware.metrics

import cats.effect.{Clock, Sync}
import com.codahale.metrics.SharedMetricRegistries
import com.codahale.metrics.jvm.{GarbageCollectorMetricSet, MemoryUsageGaugeSet, ThreadStatesGaugeSet}
import org.http4s.HttpRoutes
import org.http4s.metrics.dropwizard.Dropwizard
import org.http4s.server.middleware.Metrics

object MetricsMiddleware {
  val appRegistry = SharedMetricRegistries.getOrCreate("app")
  appRegistry.register("GC", new GarbageCollectorMetricSet())
  appRegistry.register("MEMORY", new MemoryUsageGaugeSet())
  appRegistry.register("Thread_State_Gauge", new ThreadStatesGaugeSet())

  def apply[F[_]: Sync: Clock](service: HttpRoutes[F]): HttpRoutes[F] = Metrics[F](Dropwizard(appRegistry, "server"))(service)

}


