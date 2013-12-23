package objectstorage

import java.util.UUID

/**
 *
 * @author Sergey Krauchenia
 */
class StorageContainerActionsTest extends BaseSpec {

  "Storage container" should {
    "create new folder if request correct" in {
      val containerName = UUID.randomUUID().toString
      val connection = Connection(userName, apiKey)
      val api = Api[StorageContainer](connection)
      val container = StorageContainer(containerName)
      val response = api create container
      api delete container
      response.getStatusCode should be equalTo ApiResponseCodes.CONTAINER_CREATE_OK
    }
    
    "delete existing folder if request correct" in {
      val containerName = UUID.randomUUID().toString
      val connection = Connection(userName, apiKey)
      val api = Api[StorageContainer](connection)
      val container = StorageContainer(containerName)
      val createResp = api create container
      createResp.getStatusCode should be equalTo ApiResponseCodes.CONTAINER_CREATE_OK
      val deleteResp = api delete container
      deleteResp.getStatusCode should be equalTo ApiResponseCodes.CONTAINER_DELETE_OK
    }

    "should return error code if trying to delete non existing folder" in {
      val containerName = UUID.randomUUID().toString
      val connection = Connection(userName, apiKey)
      val api = Api[StorageContainer](connection)
      val container = StorageContainer(containerName)
      val deleteResp = api delete container
      deleteResp.getStatusCode should be equalTo ApiResponseCodes.CONTAINER_DELETE_NOT_EXIST
    }
  }

}
