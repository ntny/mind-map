package com.ntny.dba

trait Command[F[_], P] {
  def exec(parameter: P): F[Int]
}
