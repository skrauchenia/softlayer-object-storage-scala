package objectstorage

import dispatch._, Defaults._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

/**
 *
 * @author Sergey Krauchenia
 */
sealed trait Connection {
  def isAuthorized: Boolean = this.isInstanceOf[Authorized]
}

private[objectstorage] case class Authorized(authToken: String, storageUrl: String) extends Connection

private[objectstorage] case class NotAuthorized(code: Int) extends Connection

object Connection extends ApiHeaders {

  def apply(userName: String, apiKey: String, dataCenter: String = "dal05",
            protocol: String = "http", network: String = "public"): Connection = {
    val authUrl = ENDPOINTS(dataCenter)(network)(protocol)
    val authHeaders = createAuthHeaders(userName, apiKey)
    val svc = url(authUrl) <:< authHeaders
    val response: Future[Response] = Http(svc)

    Try(Await.result(response, 10.seconds)) match {
      case Success(resp) =>
        resp.getStatusCode match {
          case 200 =>
            val authToken = resp.getHeader(X_AUTH_TOKEN)
            val storageUrl = resp.getHeader(X_STORAGE_URL)
            Authorized(authToken, storageUrl)
          case code => NotAuthorized(code)
        }
      case Failure(StatusCode(code)) => NotAuthorized(code)
      case Failure(e) => throw e
    }
  }

  private def createAuthHeaders(userName: String, apiKey: String): Map[String, String] = {
    Map(X_STORAGE_USER -> userName, X_STORAGE_PASS -> apiKey, "Content-Length" -> "0")
  }
}


