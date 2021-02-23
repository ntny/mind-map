package com.ntny.dba

import com.ntny.models.Link

trait LinksStorage[F[_]] {
  def put(link: Link): F[Unit]
  def all: F[List[Link]]
}