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
  /** indicates if container found */
  val CONTAINER_GET_OK = 200
  /** means that acquiring container have no content in it */
  val CONTAINER_NO_CONTENT = 204

  /** object created successfully */
  val OBJECT_CREATE_OK = 201
  /** indicates if file found */
  val OBJECT_GET_OK = 200
}

object ApiResponseCodes extends ApiResponseCodes
