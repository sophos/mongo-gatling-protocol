/**
  * Created by ryandavis on 2/24/17.
  */

package com.sophos.gatling.mongo.protocol

import io.gatling.core.protocol.ProtocolComponents
import io.gatling.core.session.Session

case class MongoComponents(mongoProtocol: MongoProtocol) extends ProtocolComponents {

  def onStart: Option[Session => Session] = None
  def onExit: Option[Session => Unit] = None
}