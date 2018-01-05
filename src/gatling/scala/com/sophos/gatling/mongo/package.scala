package com.sophos.gatling

import io.gatling.commons.validation.Success
import io.gatling.core.check.{Check, Extender, Preparer}

package object mongo {

  type MongoCheck = Check[String]

  val MongoStringExtender: Extender[MongoCheck, String] =
    (check: MongoCheck) => check

  val MongoStringPreparer: Preparer[String, CharSequence] =
    (result: String) => Success(result)

}
