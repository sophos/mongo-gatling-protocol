package com.sophos.gatling.mongo

import com.sophos.gatling.mongo.check.MongoCheckSupport
import com.sophos.gatling.mongo.process.MongoProcessBuilder
import com.sophos.gatling.mongo.protocol.{MongoProtocol, MongoProtocolBuilder, MongoProtocolBuilderBase}
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.session.Expression

import scala.language.implicitConversions

trait MongoDsl extends MongoCheckSupport {

  val mongo = MongoProtocolBuilderBase

  def makeRequest(queryName: String, query: Expression[String]) = MongoProcessBuilder(queryName, query)

  implicit def mongoProtocolBuilderToMongoProtocol(builder: MongoProtocolBuilder): MongoProtocol = builder.build

  implicit def mongoProcessBuilderToActionBuilder(builder: MongoProcessBuilder): ActionBuilder = builder.build()

}