package objectstorage

import dispatch._

/**
 *
 * @author Sergey Krauchenia
 */
case class Api(connection: Connection) {

  private[objectstorage] def asyncApi = this

  def create(obj: StorageUnit): Future[Response] = doAuthorizedAction(connection) { connection =>
    obj.create(connection)
  }

  def delete(obj: StorageUnit): Future[Response] = doAuthorizedAction(connection) { connection =>
    obj.delete(connection)
  }

  def getContainer(withName: String): Future[Option[StorageFolder]] = doAuthorizedAction(connection) { connection =>
    StorageContainer(withName).get(connection)
  }

  def getFile(fileName: String) = ???

  def doAuthorizedAction[R](conn: Connection)(block: => (Authorized) => R): R = conn match {
    case (auth: Authorized) => block(auth)
    case (notAuth: NotAuthorized) => throw new IllegalStateException("Not authorized")
  }

  object blocking {

    def create(obj: StorageUnit): Response = (asyncApi create obj)()

    def delete(obj: StorageUnit): Response = (asyncApi delete obj)()

    def getContainer(withName: String): Option[StorageFolder] = {
      (asyncApi getContainer withName)()
    }
  }
}
