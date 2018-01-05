package com.sophos.gatling.mongo.action

import com.sophos.gatling.mongo.MongoCheck
import com.sophos.gatling.mongo.protocol.{MongoComponents, MongoProtocol}
import io.gatling.core.action.Action
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.protocol.ProtocolComponentsRegistry
import io.gatling.core.session.Expression
import io.gatling.core.structure.ScenarioContext

case class MongoActionBuilder(queryName: String, query: Expression[String], checks: List[MongoCheck]) extends ActionBuilder {

  private def components(protocolComponentsRegistry: ProtocolComponentsRegistry): MongoComponents =
    protocolComponentsRegistry.components(MongoProtocol.mongoProtocolKey)

  override def build(ctx: ScenarioContext, next: Action): Action = {
    import ctx._
    val statsEngine = coreComponents.statsEngine
    val mongoComponents = components(protocolComponentsRegistry)
    MongoAction(queryName, query, checks, mongoComponents.mongoProtocol, ctx.system, statsEngine, next)
  }

}