package objectstorage

import java.util.UUID

/**
 *
 * @author Sergey Krauchenia
 */
class StorageContainerActionsTest extends BaseSpec {

  "Storage container" should {
    "create new folder if request correct" in {
      val (api, container) = createContainerAndApi()
      val response = api create container
      api delete container

      response.getStatusCode should be equalTo ApiResponseCodes.CONTAINER_CREATE_OK
    }
    
    "delete existing folder if request correct" in {
      val (api, container) = createContainerAndApi()
      val createResp = api create container
      createResp.getStatusCode should be equalTo ApiResponseCodes.CONTAINER_CREATE_OK

      val deleteResp = api delete container
      deleteResp.getStatusCode should be equalTo ApiResponseCodes.CONTAINER_DELETE_OK
    }

    "should return error code if trying to delete non existing folder" in {
      val (api, container) = createContainerAndApi()
      val deleteResp = api delete container

      deleteResp.getStatusCode should be equalTo ApiResponseCodes.CONTAINER_DELETE_NOT_EXIST
    }

    "create new cdn folder if request correct" in {
      val (api, container) = createContainerAndApi(cdn = true)
      val response = api create container
      api delete container

      response.getStatusCode should be equalTo ApiResponseCodes.CONTAINER_CREATE_OK
    }

    "delete existing cdn folder if request correct" in {
      val (api, container) = createContainerAndApi(cdn = true)
      val createResp = api create container
      createResp.getStatusCode should be equalTo ApiResponseCodes.CONTAINER_CREATE_OK

      val deleteResp = api delete container
      deleteResp.getStatusCode should be equalTo ApiResponseCodes.CONTAINER_DELETE_OK
    }

    "should return error code if trying to delete non existing cdn folder" in {
      val (api, container) = createContainerAndApi(cdn = true)
      val deleteResp = api delete container

      deleteResp.getStatusCode should be equalTo ApiResponseCodes.CONTAINER_DELETE_NOT_EXIST
    }

    "receive file info" in {
      val (api, container) = createContainerAndApi()
      val createContainerResponse = api create container
      createContainerResponse.getStatusCode must beEqualTo(ApiResponseCodes.CONTAINER_CREATE_OK)

      val objMetadata = api getContainer container.name
      api delete container

      objMetadata must beSome
      objMetadata.get.url.getPath must endWith(container.name)
    }

    "receive None if container not exists" in {
      val containerName = UUID.randomUUID().toString
      val connection = Connection(userName, apiKey)
      val api = Api(connection).blocking

      val container = api getContainer containerName
      container must beNone
    }
  }

  def createContainerAndApi(cdn: Boolean = false) = {
    val containerName = UUID.randomUUID().toString
    val connection = Connection(userName, apiKey)
    val api = Api(connection).blocking
    val container = StorageContainer(containerName)

    (api, container)
  }

}
