package com.sophos.gatling.mongo.process

import com.sophos.gatling.mongo.MongoCheck
import com.sophos.gatling.mongo.action.MongoActionBuilder
import com.sophos.gatling.mongo.check.MongoCheckSupport
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.session.Expression

case class MongoProcessBuilder(queryName: String, query: Expression[String], checks: List[MongoCheck] = Nil) extends MongoCheckSupport {

  def query(query: Expression[String]): MongoProcessBuilder = copy(query = query)

  def queryName(queryName: String): MongoProcessBuilder = copy(queryName = queryName)

  /**
    * Add a check that will be performed on the response payload before giving Gatling on OK/KO response
    */
  def check(mongoChecks: MongoCheck*) = copy(checks = checks ::: mongoChecks.toList)

  def build(): ActionBuilder = MongoActionBuilder(queryName, query, checks)
}
