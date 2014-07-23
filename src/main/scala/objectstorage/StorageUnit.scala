package objectstorage

import dispatch._, Defaults._
import java.io.File

/**
 *
 * @author Sergey Krauchenia
 */
sealed trait StorageUnit extends ApiHeaders with ApiResponseCodes {

  type UnitMetadata <: StorageUnitMetadata

  def create(connection: Authorized): Future[Response]

  def delete(connection: Authorized): Future[Response]

  def get(connection: Authorized): Future[Option[UnitMetadata]]

  protected def get(connection: Authorized, unitFactory: Response => UnitMetadata): Future[Option[UnitMetadata]] = {
    val futureResponse = doGet(constructUnitUrl(connection), connection)
    futureResponse.map { response =>
      response.getStatusCode match {
        case CONTAINER_GET_OK | CONTAINER_NO_CONTENT => Some(unitFactory(response))
        case _ => None
      }
    }
  }

  def name: String

  protected def constructUnitUrl(connection: Authorized): String

  def getAuthHeader(connection: Authorized) = {
    X_AUTH_TOKEN -> connection.authToken
  }

  protected def doPut(requestUrl: String, connection: Authorized,
                      headers: Map[String, String] = Map.empty): Future[Response] = {
    performRequest(requestUrl, connection, headers, "PUT")
  }

  protected def doGet(requestUrl: String, connection: Authorized,
                      headers: Map[String, String] = Map.empty): Future[Response] = {
    performRequest(requestUrl, connection, headers, "GET")
  }

  protected def doPost(requestUrl: String, connection: Authorized,
                      headers: Map[String, String] = Map.empty): Future[Response] = {
    performRequest(requestUrl, connection, headers, "POST")
  }

  protected def doHead(requestUrl: String, connection: Authorized,
                      headers: Map[String, String] = Map.empty): Future[Response] = {
    performRequest(requestUrl, connection, headers, "HEAD")
  }

  protected def doDelete(requestUrl: String, connection: Authorized,
                       headers: Map[String, String] = Map.empty): Future[Response] = {
    performRequest(requestUrl, connection, headers, "DELETE")
  }

  protected def performRequest(requestUrl: String, connection: Authorized,
                               headers: Map[String, String] = Map.empty, method: String): Future[Response] = {

    val headersWithAuth = headers + getAuthHeader(connection)
    val svc = url(requestUrl).setMethod(method) <:< headersWithAuth
    Http(svc)
  }

}

case class StorageContainer(name: String, cdn: Boolean = false) extends StorageUnit {

  type UnitMetadata = StorageFolder

  val cdnHeader: (String, String) = {
    if (cdn) X_CONTAINER_READ -> ".r:*"
    else "n" -> "0"
  }

  def constructUnitUrl(connection: Authorized) = s"${connection.storageUrl}/$name"

  def create(connection: Authorized): Future[Response] = {
    val headers = Map(cdnHeader)
    doPut(constructUnitUrl(connection), connection, headers)
  }

  def delete(connection: Authorized): Future[Response] = {
    doDelete(constructUnitUrl(connection), connection)
  }

  def get(connection: Authorized): Future[Option[StorageFolder]] = {
    get(connection, response => StorageFolder(response.getHeaders, response.getUri))
  }
}

case class StorageObject(name: String, filePath: String, container: StorageContainer) extends StorageUnit {

  type UnitMetadata = StorageFile

  def constructUnitUrl(connection: Authorized) = {
    container.constructUnitUrl(connection) + "/" + name
  }
  
  def create(connection: Authorized): Future[Response] = {
    val headersWithAuth = Map(getAuthHeader(connection))
    val svc = url(constructUnitUrl(connection)).PUT <:< headersWithAuth <<< new File(filePath)
    Http(svc)
  }

  def delete(connection: Authorized): Future[Response] = {
    doDelete(constructUnitUrl(connection), connection)
  }

  def get(connection: Authorized): Future[Option[StorageFile]] = {
    get(connection, response => StorageFile(response.getHeaders, response.getUri))
  }
}
