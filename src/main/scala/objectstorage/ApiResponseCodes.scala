package objectstorage

/**
 * response codes specific to softlayer object storage
 * @author Sergey Krauchenia
 */
trait ApiResponseCodes {

  /** container created successfully */
  val CONTAINER_CREATE_OK = 201
  /** container didn't created because it already exists */
  val CONTAINER_CREATE_EXISTS = 202
}
