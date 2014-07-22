/**
 *
 * @author Sergey Krauchenia
 */
package object objectstorage {

  type Future[+T] = dispatch.Future[T]
  type Response = com.ning.http.client.Response

  val ENDPOINTS = Map (
    "dal05" -> Map (
      "public" -> Map (
        "http" -> "http://dal05.objectstorage.softlayer.net/auth/v1.0",
        "https" -> "https://dal05.objectstorage.softlayer.net/auth/v1.0"
      ),
      "private" -> Map (
        "http" -> "http://dal05.objectstorage.service.networklayer.com/auth/v1.0",
        "https" -> "https://dal05.objectstorage.service.networklayer.com/auth/v1.0"
      )
    ),
    "ams01" -> Map (
      "public" -> Map (
        "http" -> "http://ams01.objectstorage.softlayer.net/auth/v1.0",
        "https" -> "https://ams01.objectstorage.softlayer.net/auth/v1.0"
      ),
      "private" -> Map (
        "http" -> "http://ams01.objectstorage.service.networklayer.com/auth/v1.0",
        "https" -> "https://ams01.objectstorage.service.networklayer.com/auth/v1.0"
      )
    ),
    "sng01" -> Map (
      "public" -> Map (
        "http" -> "http://sng01.objectstorage.softlayer.net/auth/v1.0",
        "https" -> "https://sng01.objectstorage.softlayer.net/auth/v1.0"
      ),
      "private" -> Map (
        "http" -> "http://sng01.objectstorage.service.networklayer.com/auth/v1.0",
        "https" -> "https://sng01.objectstorage.service.networklayer.com/auth/v1.0"
      )
    )
  )

}
