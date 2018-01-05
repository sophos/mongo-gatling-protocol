package com.sophos.gatling.mongo.check

import java.io.StringReader

import com.sophos.gatling.mongo.MongoCheck
import io.gatling.commons.validation._
import io.gatling.core.check._
import io.gatling.core.check.extractor.xpath._
import org.xml.sax.InputSource



object MongoXPathCheckBuilder extends XPathCheckBuilder[MongoCheck, String] {

  private val ErrorMapper: String => String = "Could not parse response into a DOM Document: " + _

  def preparer[T](f: InputSource => T)(payload: String): Validation[Option[T]] =
    safely(ErrorMapper) {
      Some(f(new InputSource(new StringReader(payload)))).success
    }

  val CheckBuilder: Extender[MongoCheck, String] = (wrapped: MongoCheck) => wrapped
}