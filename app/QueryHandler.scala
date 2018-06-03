package com.knoldus

import com.datastax.driver.core.{ResultSet, Session}
import com.knoldus.utils.ConfigReader

import scala.collection.JavaConversions

case class EmployeeRecord(id: Int, name: String, age: Int)

trait QueryHandler {

  /**
    * This method demonstrate all CRUD operations one by one
    * @param session
    */
  def runQuery(session: Session): Unit ={
    val keyspace = ConfigReader.getKeyspaceName
    val tableName = ConfigReader.getTableName
    val selectQuery = s"select * from $tableName"
    val insertQuery = s"insert into $keyspace.$tableName(id,age,name) values(2,24,'geetu')"
    val updateQuery = s"update $keyspace.$tableName set age=23 where id=2"
    val deleteQuery = s"delete from $keyspace.$tableName where id=2"

    println("\n\nRecords in table Initially...")
    println(displayResult(selectQuery, session))

    // Inserting into table
    println("\n\nInserting one Record ...")
    CassandraConnector.executeQuery(session, insertQuery)
    println(displayResult(selectQuery, session))

    println("\n\nUpdating Inserted Record ...")
    CassandraConnector.executeQuery(session, updateQuery)
    println(displayResult(selectQuery, session))

    println("\n\nDeleting Inserted Record ...")
    CassandraConnector.executeQuery(session, deleteQuery)
    println(displayResult(selectQuery, session))
  }

  /**
    * This method is responsible to fetch the list of records present in the table
    *
    * @param selectQuery :  This is the query to fetch records from cassandra table
    * @param session : It is the cassandra session
    * @return
    */
  def displayResult(selectQuery: String, session: Session): List[EmployeeRecord] = {
    val resultSet: ResultSet = CassandraConnector.executeQuery(session, selectQuery)
    val iterator = JavaConversions.asScalaIterator(resultSet.iterator)
    val records = iterator map { row =>
      EmployeeRecord(row.getInt("id"), row.getString("name"), row.getInt("age"))
//      println(s"[ id: ${row.getInt("id")}, name : ${row.getString("name")}, age : ${row.getInt("age")} ]")
    }
    records.toList
  }

}

object QueryHandler extends QueryHandler
