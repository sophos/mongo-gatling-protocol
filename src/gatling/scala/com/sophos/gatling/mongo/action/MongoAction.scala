package com.sophos.gatling.mongo.action

import akka.actor.{ActorSystem, Props}
import com.mongodb._
import com.sophos.gatling.mongo.MongoCheck
import com.sophos.gatling.mongo.protocol.MongoProtocol
import io.gatling.commons.stats.Status
import io.gatling.commons.validation._
import io.gatling.core.action._
import io.gatling.core.session.{Expression, Session}
import io.gatling.core.stats.StatsEngine
import io.gatling.core.stats.message.ResponseTimings
import io.gatling.core.util.NameGen
import org.bson.BsonDocument


object MongoAction extends NameGen {

  def apply(queryName: String, query: Expression[String], checks: List[MongoCheck], protocol: MongoProtocol, system: ActorSystem, statsEngine: StatsEngine, next: Action) = {
    val actor = system.actorOf(MongoActionActor.props(queryName, query, checks, protocol, statsEngine, next))
    new ExitableActorDelegatingAction(genName("Mongo"), statsEngine, next, actor)
  }
}

object MongoActionActor {
  def props(queryName: String, query: Expression[String], checks: List[MongoCheck], protocol: MongoProtocol, statsEngine: StatsEngine, next: Action): Props =
    Props(new MongoActionActor(queryName, query, checks, protocol, statsEngine, next))
}

class MongoActionActor(
                        queryName: String,
                        query: Expression[String],
                        checks: List[MongoCheck],
                        protocol: MongoProtocol,
                        val statsEngine: StatsEngine,
                        val next: Action
                      ) extends ActionActor {
  val options = new MongoClientOptions.Builder().connectionsPerHost(10000).readPreference(protocol.readPreference).build()
  val client = new MongoClient(protocol.serverList, protocol.credentials, options)
  override def execute(session: Session) = {
    val database = client.getDatabase(protocol.databaseName)
    var mongoQueryString = "Default Query"
    query(session).flatMap { resolvedQuery =>
      mongoQueryString = resolvedQuery
      this.success
    }

    var optionalThrowable: Option[Throwable] = None
    val startTime = now()
    try {
      val dbObject = BsonDocument.parse(mongoQueryString)
      logger.trace("DBOBJECT: " + dbObject.toJson())
      database.runCommand(dbObject)
    } catch {
      case t: Throwable => optionalThrowable = Some(t)
    }
    val endTime = now()
    val timings = ResponseTimings(startTime, endTime)

    if (optionalThrowable.isEmpty) {
      statsEngine.logResponse(session, queryName, timings, Status("OK"), None, None)
      next ! session
    } else {
      val throwable = optionalThrowable.get
      statsEngine.logResponse(session, queryName, timings, Status("KO"), None, Some(throwable.getMessage))
      next ! session.markAsFailed
    }

  }

  @inline
  private def now() = System.currentTimeMillis()

}
