package objectstorage

import dispatch._, Defaults._
import com.ning.http.client.Response

/**
 *
 * @author Sergey Krauchenia
 */
sealed trait StorageUnit extends ApiHeaders {

  def create(connection: Authorized): Future[Response]

  def delete(connection: Authorized): Future[Response]

  def getAuthHeader(connection: Authorized) = {
    X_AUTH_TOKEN -> connection.authToken
  }

  protected def doPut(requestUrl: String, connection: Connection,
                      headers: Map[String, String] = Map()): Future[Response] = {
    val svc = url(requestUrl).PUT <:< headers
    Http(svc)
  }

  protected def doGet(requestUrl: String, connection: Connection,
                      headers: Map[String, String] = Map()): Future[Response] = {
    val svc = url(requestUrl).GET <:< headers
    Http(svc)
  }

  protected def doPost(requestUrl: String, connection: Connection,
                      headers: Map[String, String] = Map()): Future[Response] = {
    val svc = url(requestUrl).POST <:< headers
    Http(svc)
  }

  protected def doHead(requestUrl: String, connection: Connection,
                      headers: Map[String, String] = Map()): Future[Response] = {
    val svc = url(requestUrl).HEAD <:< headers
    Http(svc)
  }

  protected def doDelete(requestUrl: String, connection: Connection,
                       headers: Map[String, String] = Map()): Future[Response] = {
    val svc = url(requestUrl).HEAD <:< headers
    Http(svc)
  }

}

case class StorageContainer(name: String, cdn: Boolean = false) extends StorageUnit {

  val cdnHeader: (String, String) = {
    if (cdn) X_CONTAINER_READ -> ".r:*"
    else "" -> ""
  }

  def createContainerUrl(connection: Authorized) = s"${connection.storageUrl}/$name"

  def create(connection: Authorized): Future[Response] = {
    val headers = Map(getAuthHeader(connection), cdnHeader)
    doPut(createContainerUrl(connection), connection, headers)
  }

  def delete(connection: Authorized): Future[Response] = {
    doDelete(createContainerUrl(connection), connection, Map(getAuthHeader(connection)))
  }
}

case class StorageObject(name: String, container: StorageContainer) extends StorageUnit {

  def create(connection: Authorized): Future[Response] = ???

  def delete(connection: Authorized): Future[Response] = ???
}
