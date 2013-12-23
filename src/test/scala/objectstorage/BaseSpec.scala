package objectstorage

import com.typesafe.config.{ConfigFactory, Config}
import org.specs2.mutable.Specification

/**
 *
 * @author Sergey Krauchenia
 */
trait BaseSpec extends Specification {

  lazy val config: Config = {
    ConfigFactory.load()
  }
  
  protected val userName = getProperty("username")
  protected val apiKey = getProperty("apikey")

  private def getProperty(propertyName: String): String = {
    val property = config.getString(propertyName)
    if (property == "") throw new IllegalStateException(s"property $propertyName can't be empty. Please specify it in application.properties")
    else property
  }
}
