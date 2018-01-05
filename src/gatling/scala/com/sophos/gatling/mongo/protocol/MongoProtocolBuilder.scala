package com.sophos.gatling.mongo.protocol

object MongoProtocolBuilderBase {
  def hostName(hostName: String) = MongoProtocolBuilderUserNameStep(hostName)
}

case class MongoProtocolBuilderUserNameStep(hostName: String) {
  def userName(userName: String) = MongoProtocolBuilderPasswordStep(hostName, userName)
}

case class MongoProtocolBuilderPasswordStep(hostName: String, userName: String) {
  def password(password: String) = MongoProtocolBuilderDatabaseNameStep(hostName, userName, password)
}

case class MongoProtocolBuilderDatabaseNameStep(hostName: String, userName: String, password: String) {
  def databaseName(databaseName: String) = MongoProtocolBuilderReadFromSecondaryStep(hostName, userName, password, databaseName)
}

case class MongoProtocolBuilderReadFromSecondaryStep(hostName: String, userName: String, password: String, databaseName: String) {
  def readFromSecondary(readFromSecondary: Boolean) = MongoProtocolBuilder(hostName, userName, password, databaseName, readFromSecondary)
}

case class MongoProtocolBuilder(mongoHostName: String, mongoUserName: String, mongoPassword: String, mongoDatabaseName: String, mongoReadFromSecondary: Boolean) {

  def build = MongoProtocol(
    hostName = mongoHostName,
    userName = mongoUserName,
    password = mongoPassword,
    databaseName = mongoDatabaseName,
    readFromSecondary = mongoReadFromSecondary
  )
}