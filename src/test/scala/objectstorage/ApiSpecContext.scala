package objectstorage

import java.util.UUID

import com.typesafe.config.{ConfigFactory, Config}
import org.specs2.mutable.BeforeAfter

/**
 *
 * @author Sergey Krauchenia
 */
class ApiSpecContext(val cdn: Boolean = false) extends BeforeAfter {

  def before { }

  def after { }

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

  val containerName = UUID.randomUUID().toString
  implicit val connection = Connection(userName, apiKey)
  val api = Api.blocking
  val container = NewRemoteFolder(containerName, cdn)
}
