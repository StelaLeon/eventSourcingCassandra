package com.knoldus.utils

import com.typesafe.config.ConfigFactory

object ConfigReader {

  val conf = ConfigFactory.load

  def getCassandraHost = conf.getString("cassandra.host")
  def getCassandraPort = conf.getString("cassandra.port")
  def getKeyspaceName = conf.getString("cassandra.keyspace")
  def getTableName = conf.getString("cassandra.tablename")
}
