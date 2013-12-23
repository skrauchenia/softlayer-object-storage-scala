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
  /** indicates if container deleted successfully */
  val CONTAINER_DELETE_OK = 204
  /** container didn't deleted because it doesn't exists */
  val CONTAINER_DELETE_NOT_EXIST = 404
}

object ApiResponseCodes extends ApiResponseCodes
