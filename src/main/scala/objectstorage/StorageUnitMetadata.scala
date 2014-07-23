package objectstorage

import java.net.URI

/**
 *
 * @author Sergey Krauchenia
 */
sealed trait StorageUnitMetadata extends ApiHeaders {

  val url: URI
  protected val headers: ResponseHeaders
  lazy val length = getHeaderValue("Content-Length")

  protected def getHeaderValue(key: String) = headers.getFirstValue(key)

}

case class StorageFolder(protected val headers: ResponseHeaders, url: URI) extends StorageUnitMetadata {

  lazy val objectsCount = getHeaderValue("X-Container-Object-Count").toLong
  lazy val objectsLength = getHeaderValue("X-Container-Bytes-Used").toLong
  lazy val cdnEnabled = getHeaderValue(X_CONTAINER_READ) == ".r:*"
}

case class StorageFile(protected val headers: ResponseHeaders, url: URI) extends StorageUnitMetadata {

  lazy val content: Array[Byte] = ???
}
