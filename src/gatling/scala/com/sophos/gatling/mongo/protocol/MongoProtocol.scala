/**
  * Created by ryandavis on 2/24/17.
  */
package com.sophos.gatling.mongo.protocol

import akka.actor.ActorSystem
import com.mongodb.{MongoCredential, ReadPreference, ServerAddress}
import io.gatling.core.CoreComponents
import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.protocol.{Protocol, ProtocolKey}

import scala.collection.JavaConversions._

object MongoProtocol {

  val mongoProtocolKey = new ProtocolKey {

    type Protocol = MongoProtocol
    type Components = MongoComponents

    def protocolClass: Class[io.gatling.core.protocol.Protocol] = classOf[MongoProtocol].asInstanceOf[Class[io.gatling.core.protocol.Protocol]]

    def defaultProtocolValue(configuration: GatlingConfiguration): MongoProtocol = throw new IllegalStateException("Can't provide a default value for MongoProtocol")

    def newComponents(system: ActorSystem, coreComponents: CoreComponents): MongoProtocol => MongoComponents = {
      mongoProtocol: MongoProtocol => MongoComponents(mongoProtocol)
    }
  }
}

case class MongoProtocol(
                        hostName: String,
                        userName: String,
                        password: String,
                        databaseName: String,
                        readFromSecondary: Boolean
                      ) extends Protocol {
  val servers = hostName.split(",")
  val serverList = new java.util.ArrayList[ServerAddress]()
  val credential = MongoCredential.createCredential(userName, databaseName, password.toArray)
  val credentials = new java.util.ArrayList[MongoCredential]()
  for (server <- servers) serverList += new ServerAddress(server)
  if(credentials.isEmpty) {
    credentials.add(credential)
  }
  var readPreference = ReadPreference.primary()
  if(readFromSecondary){
    readPreference = ReadPreference.secondaryPreferred()
  }
  type Components = MongoComponents
}