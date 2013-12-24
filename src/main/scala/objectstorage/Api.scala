package objectstorage

import dispatch._, Defaults._
import com.ning.http.client.Response

/**
 *
 * @author Sergey Krauchenia
 */
case class Api(connection: Connection) {

  def create(obj: StorageUnit): Response = doAuthorizedAction(connection) { connection =>
    val future = obj.create(connection)
    extractResponse(future)
  }

  def delete(obj: StorageUnit): Response = doAuthorizedAction(connection) { connection =>
    val future = obj.delete(connection)
    extractResponse(future)
  }

  def extractResponse(future: Future[Response]): Response = {
    val response = for (f <- future) yield f

    response()
  }

  def doAuthorizedAction[R](conn: Connection)(block: => (Authorized) => R): R = conn match {
    case (auth: Authorized) => block(auth)
    case (notAuth: NotAuthorized) => throw new IllegalStateException("Not authorized")
  }
}
