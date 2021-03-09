package com.ntny.dba

trait Query[F[_], R] {
  def exec(): F[R]
}
