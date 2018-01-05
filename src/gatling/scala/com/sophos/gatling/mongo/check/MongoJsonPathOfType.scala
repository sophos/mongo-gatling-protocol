package com.sophos.gatling.mongo.check

import com.sophos.gatling.mongo._
import io.gatling.core.check.extractor.jsonpath._
import io.gatling.core.check.{DefaultMultipleFindCheckBuilder, Preparer}
import io.gatling.core.json.JsonParsers
import io.gatling.core.session.{Expression, RichExpression}

trait MongoJsonPathOfType {
  self: MongoJsonPathCheckBuilder[String] =>

  def ofType[X: JsonFilter](implicit extractorFactory: JsonPathExtractorFactory) = new MongoJsonPathCheckBuilder[X](path, jsonParsers)
}

object MongoJsonPathCheckBuilder {

  val CharsParsingThreshold = 200 * 1000

  def preparer(jsonParsers: JsonParsers): Preparer[String, Any] =
    response => {
      if (response.length() > CharsParsingThreshold || jsonParsers.preferJackson)
        jsonParsers.safeParseJackson(response)
      else
        jsonParsers.safeParseBoon(response)
    }

  def jsonPath(path: Expression[String])(implicit extractorFactory: JsonPathExtractorFactory, jsonParsers: JsonParsers) =
    new MongoJsonPathCheckBuilder[String](path, jsonParsers) with MongoJsonPathOfType
}

class MongoJsonPathCheckBuilder[X: JsonFilter](
                                                 private[check] val path:        Expression[String],
                                                 private[check] val jsonParsers: JsonParsers
                                               )(implicit extractorFactory: JsonPathExtractorFactory)
  extends DefaultMultipleFindCheckBuilder[MongoCheck, String, Any, X](
    MongoStringExtender,
    MongoJsonPathCheckBuilder.preparer(jsonParsers)
  ) {

  import extractorFactory._

  def findExtractor(occurrence: Int) = path.map(newSingleExtractor[X](_, occurrence))
  def findAllExtractor = path.map(newMultipleExtractor[X])
  def countExtractor = path.map(newCountExtractor)
}