package com.sophos.gatling.mongo.check

import com.sophos.gatling.mongo.MongoCheck
import com.sophos.gatling.mongo._
import io.gatling.core.check.DefaultMultipleFindCheckBuilder
import io.gatling.core.check.extractor.regex._
import io.gatling.core.session.{Expression, RichExpression}


trait MongoRegexOfType { self: MongoRegexCheckBuilder[String] =>

  def ofType[X: GroupExtractor](implicit extractorFactory: RegexExtractorFactory) = new MongoRegexCheckBuilder[X](expression)
}

object MongoRegexCheckBuilder {

  def regex(expression: Expression[String])(implicit extractorFactory: RegexExtractorFactory) =
    new MongoRegexCheckBuilder[String](expression) with MongoRegexOfType
}

class MongoRegexCheckBuilder[X: GroupExtractor](private[check] val expression: Expression[String])(implicit extractorFactory: RegexExtractorFactory)
  extends DefaultMultipleFindCheckBuilder[MongoCheck, String, CharSequence, X](MongoStringExtender, MongoStringPreparer) {
  import extractorFactory._

  def findExtractor(occurrence: Int) = expression.map(newSingleExtractor[X](_, occurrence))
  def findAllExtractor = expression.map(newMultipleExtractor[X])
  def countExtractor = expression.map(newCountExtractor)
}