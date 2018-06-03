package com.knoldus

import com.datastax.driver.core.{Cluster, ResultSet, Session}
import com.knoldus.utils.ConfigReader

trait CassandraConnector {

  /**
    * This method creates the cluster by configuring port number and host
    * @return Cassandra Cluster
    */
  def getCasssandraBuilder: Cluster = {
    Cluster.builder()
      .addContactPoint(ConfigReader.getCassandraHost)
      .withPort(ConfigReader.getCassandraPort.toInt)
      .build()
  }

  /**
    * This method connects to cassandra keyspace
    *
    * @param keySpaceName
    * @param cluster
    * @return session
    */
  def getSession(keySpaceName: String, cluster: Cluster): Session = {
    cluster.connect(keySpaceName)
  }

  def executeQuery(session: Session, query: String): ResultSet = {
    session.execute(query)
  }

}

object CassandraConnector extends CassandraConnector
