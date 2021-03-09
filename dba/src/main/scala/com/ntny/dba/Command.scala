package com.ntny.dba

trait Command[F[_]] {
  def exec(): F[Int]
}
