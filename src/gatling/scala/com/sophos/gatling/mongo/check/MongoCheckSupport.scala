package com.sophos.gatling.mongo.check

import io.gatling.core.check.extractor.jsonpath.JsonPathExtractorFactory
import io.gatling.core.check.extractor.regex._
import io.gatling.core.check.extractor.xpath.{JdkXPathExtractorFactory, SaxonXPathExtractorFactory}
import io.gatling.core.json.JsonParsers
import io.gatling.core.session.Expression

trait MongoCheckSupport {
  def regex(expression: Expression[String])(implicit extractorFactory: RegexExtractorFactory) =
    MongoRegexCheckBuilder.regex(expression)

  def xpath(expression: Expression[String], namespaces: List[(String, String)] = Nil)(implicit extractorFactory: SaxonXPathExtractorFactory, jdkXPathExtractorFactory: JdkXPathExtractorFactory) =
    MongoXPathCheckBuilder.xpath(expression, namespaces)

  def jsonPath(path: Expression[String])(implicit extractorFactory: JsonPathExtractorFactory, jsonParsers: JsonParsers) =
    MongoJsonPathCheckBuilder.jsonPath(path)

  def customCheck = MongoCustomCheck

}