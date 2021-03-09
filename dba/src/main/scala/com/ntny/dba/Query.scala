package com.ntny.dba

trait Query[F[_], P, R] {
  def exec(parameter: P): F[R]
}
