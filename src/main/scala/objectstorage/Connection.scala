package objectstorage

import dispatch._, Defaults._
import com.ning.http.client
import com.ning.http.client.Response

/**
 *
 * @author Sergey Krauchenia
 */
sealed trait Connection

final case class Authorized(authUrl: String) extends Connection

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
        println(resp)
        resp.getStatusCode match {
          case 200 => Authorized(JsonResponse(resp)("storage")(network))
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

import scala.util.parsing.json.JSON

object JsonResponse extends (client.Response => Map[String, Map[String, String]]) {

  def apply(response: Response): Map[String, Map[String, String]] = {
    JSON.parseFull(response.getResponseBody).getOrElse(Map()).asInstanceOf[Map[String, Map[String, String]]]
  }
}


