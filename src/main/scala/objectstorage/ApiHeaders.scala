package objectstorage

/**
 * Names of http headers which are using for
 * requests to object storage API
 *
 * @author Sergey Krauchenia
 */
trait ApiHeaders {

  val USERNAME = "x-auth-user"
  val PASSWORD = "x-auth-key"
  val X_AUTH_TOKEN = "X-Auth-Token"
  val X_STORAGE_URL = "X-Storage-Url"
  val X_COPY_FROM = "X-Copy-From"
  val X_OBJECT_META = "X-Object-Meta-"
  val X_CDN_MANAGEMENT_URL = "X-CDN-Management-URL"
  val X_CONTENT = "X-Content"
  val X_CONTEXT = "X-Context"
  val X_CDN_URL = "X-cdn-url"
  val X_CDN_SSL_URL = "X-cdn-ssl-url"
  val X_CDN_STREAM_HTTP_URL = "X-cdn-stream-http-url"
  val X_CDN_STREAM_FLASH_URL = "X-cdn-stream-flash-url"
  val X_CDN_TTL = "X-TTL"
  val X_CDN_ENABLED = "X-CDN-Enabled"
  val X_STORAGE_USER = "X-Storage-User"
  val X_STORAGE_PASS = "X-Storage-Pass"

}
