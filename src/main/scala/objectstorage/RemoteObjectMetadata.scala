package objectstorage

import java.net.URI

/**
 *
 * @author Sergey Krauchenia
 */
sealed trait RemoteObjectMetadata extends ApiHeaders {

  val url: URI
  protected val headers: ResponseHeaders
  lazy val length = getHeaderValue("Content-Length")

  protected def getHeaderValue(key: String) = headers.getFirstValue(key)

}

case class RemoteFolderMetadata(protected val headers: ResponseHeaders, url: URI) extends RemoteObjectMetadata {

  lazy val objectsCount = getHeaderValue("X-Container-Object-Count").toLong
  lazy val objectsLength = getHeaderValue("X-Container-Bytes-Used").toLong
  lazy val cdnEnabled = getHeaderValue(X_CONTAINER_READ) == ".r:*"
}

case class RemoteFileMetadata(protected val headers: ResponseHeaders, url: URI) extends RemoteObjectMetadata {

  lazy val content: Array[Byte] = ???
}

object RemoteObjectMetadata {

  def apply(headers: ResponseHeaders, url: URI) = {
    val isFolder = headers.containsKey(ApiHeaders.X_CONTAINER_READ)
    if(isFolder) RemoteFileMetadata(headers, url)
    else RemoteFileMetadata(headers, url)
  }
}
