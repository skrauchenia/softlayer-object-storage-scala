package objectstorage

import dispatch._, Defaults._
import com.ning.http.client.Response

/**
 *
 * @author Sergey Krauchenia
 */
sealed trait Connection

final case class Authorized(authToken: String, storageUrl: String) extends Connection

final case class NotAuthorized(code: Int) extends Connection

object Connection extends ApiHeaders {

  def apply(userName: String, apiKey: String, dataCenter: String = "dal05",
            protocol: String = "http", network: String = "public"): Connection = {
    val authUrl = ENDPOINTS(dataCenter)(network)(protocol)
    val authHeaders = createAuthHeaders(userName, apiKey)
    val svc = url(authUrl) <:< authHeaders
    val response: Either[Throwable, Response] = Http(svc).either()

    response match {
      case Right(resp) =>
        resp.getStatusCode match {
          case 200 =>
            val authToken = resp.getHeader(X_AUTH_TOKEN)
            val storageUrl = resp.getHeader(X_STORAGE_URL)
            Authorized(authToken, storageUrl)
          case code => NotAuthorized(code)
        }
      case Left(StatusCode(code)) => NotAuthorized(code)
      case Left(e) => throw new RuntimeException(e)
      case (e: Exception) => throw new RuntimeException("Failed to perform auth operation", e)
    }
  }

  private def createAuthHeaders(userName: String, apiKey: String): Map[String, String] = {
    Map(X_STORAGE_USER -> userName, X_STORAGE_PASS -> apiKey, "Content-Length" -> "0")
  }
}


