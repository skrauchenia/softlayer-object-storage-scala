package objectstorage

import java.io.{FileNotFoundException, File}

import dispatch._, Defaults._

/**
 *
 * @author Sergey Krauchenia
 */
object Api extends ApiHeaders with ApiResponseCodes {

  private[objectstorage] def asyncApi = this

  def create(newRemoteFile: NewRemoteFile)(implicit connection: Connection): Future[Response] = doAuthorizedAction { implicit authorized =>
    val headersWithAuth = Map(authHeader)
    val svc = url(newRemoteFile.constructObjectUrl).PUT <:< headersWithAuth <<< new File(newRemoteFile.localFilePath)
    Http(svc)
  }

  def create(newRemoteFolder: NewRemoteFolder)(implicit connection: Connection): Future[Response] = doAuthorizedAction { implicit authorized =>
    val headers = Map(cdnHeader(newRemoteFolder.cdn))
    internal.doPut(newRemoteFolder.constructObjectUrl, headers)
  }

  def delete(objectPath: RemoteObjectPath)(implicit connection: Connection): Future[Response] = doAuthorizedAction { implicit authorized =>
    internal.doDelete(objectPath.constructObjectUrl)
  }

  // TODO: handle big files
  def download(objectPath: RemoteObjectPath)(implicit connection: Connection): Future[Array[Byte]] = doAuthorizedAction { implicit authorized =>
    val futureResponse = internal.doGet(objectPath.constructObjectUrl)

    futureResponse.map { response =>
      response.getStatusCode match {
        case OBJECT_GET_OK => response.getResponseBodyAsBytes
        case OBJECT_NOT_FOUND => throw new FileNotFoundException(s"File with path ${objectPath.constructObjectUrl} not found")
        case code => throw new Exception(s"${response.getStatusText}:$code -  Failed to download file ${objectPath.constructObjectUrl}")
      }
    }
  }

  def getMetadata(objectPath: RemoteObjectPath)(implicit connection: Connection): Future[Option[RemoteObjectMetadata]] = doAuthorizedAction { implicit authorized =>
    val futureResponse = internal.doHead(objectPath.constructObjectUrl)

    futureResponse.map { response =>
      response.getStatusCode match {
        case CONTAINER_GET_OK | CONTAINER_NO_CONTENT | OBJECT_GET_OK => Some(RemoteObjectMetadata(response.getHeaders, response.getUri))
        case _ => None
      }
    }
  }

  private def doAuthorizedAction[R](block: Authorized => R)(implicit connection: Connection): R = connection match {
    case (auth: Authorized) => block(auth)
    case (notAuth: NotAuthorized) => throw new IllegalStateException("Not authorized")
  }

  object blocking {

    def create(newRemoteFile: NewRemoteFile)(implicit connection: Connection): Response =
      (asyncApi create newRemoteFile)(connection)()

    def create(newRemoteFolder: NewRemoteFolder)(implicit connection: Connection): Response =
      (asyncApi create newRemoteFolder)(connection)()

    def delete(objectPath: RemoteObjectPath)(implicit connection: Connection): Response =
      (asyncApi delete objectPath)(connection)()

    def getMetadata(objectPath: RemoteObjectPath)(implicit connection: Connection): Option[RemoteObjectMetadata] =
      (asyncApi getMetadata objectPath)(connection)()

    def download(objectPath: RemoteObjectPath)(implicit connection: Connection): Array[Byte] =
      (asyncApi download objectPath)(connection)()
  }

  private object internal {

    def doPut(requestUrl: String, headers: Map[String, String] = Map.empty)(implicit authorized: Authorized): Future[Response] = {
      performRequest(requestUrl, headers, "PUT")
    }

    def doGet(requestUrl: String, headers: Map[String, String] = Map.empty)(implicit authorized: Authorized): Future[Response] = {
      performRequest(requestUrl, headers, "GET")
    }

    def doPost(requestUrl: String, headers: Map[String, String] = Map.empty)(implicit authorized: Authorized): Future[Response] = {
      performRequest(requestUrl, headers, "POST")
    }

    def doHead(requestUrl: String, headers: Map[String, String] = Map.empty)(implicit authorized: Authorized): Future[Response] = {
      performRequest(requestUrl, headers, "HEAD")
    }

    def doDelete(requestUrl: String, headers: Map[String, String] = Map.empty)(implicit authorized: Authorized): Future[Response] = {
      performRequest(requestUrl, headers, "DELETE")
    }

    def performRequest(requestUrl: String, headers: Map[String, String] = Map.empty, method: String)(implicit authorized: Authorized): Future[Response] = {

      val headersWithAuth = headers + authHeader
      val svc = url(requestUrl).setMethod(method) <:< headersWithAuth
      Http(svc)
    }

  }
}
