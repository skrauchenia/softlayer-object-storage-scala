package objectstorage

import dispatch._, Defaults._
import com.ning.http.client.Response

/**
 *
 * @author Sergey Krauchenia
 */
case class Api[T <: StorageUnit](connection: Connection) {

  type Status = Int

  def create(obj: T): Status = doAuthorizedAction(connection) { connection =>
    val future = obj.create(connection)
    extractResponse(future).getStatusCode
  }

  def delete(obj: T): Unit = doAuthorizedAction(connection) { connection =>
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
