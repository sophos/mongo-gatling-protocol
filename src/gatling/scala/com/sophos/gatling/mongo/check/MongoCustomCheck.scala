package com.sophos.gatling.mongo.check

import com.sophos.gatling.mongo.MongoCheck
import io.gatling.commons.validation._
import io.gatling.core.check.CheckResult
import io.gatling.core.session.Session

import scala.collection.mutable

case class MongoCustomCheck(func: String => Boolean, failureMessage: String = "Kinesis check failed") extends MongoCheck {
  def check(response: String, session: Session)(implicit cache: mutable.Map[Any, Any]): Validation[CheckResult] = {
    func(response) match {
      case true => CheckResult.NoopCheckResultSuccess
      case _ => Failure(failureMessage)
    }
  }
}