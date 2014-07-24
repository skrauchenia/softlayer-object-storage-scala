package objectstorage

/**
 *
 * @author Sergey Krauchenia
 */
class StorageContainerActionsTest extends BaseSpec {

  "Storage container" should {
    "create new folder if request correct" in new ApiSpecContext {
      val response = api create container
      api delete container

      response.getStatusCode should be equalTo ApiResponseCodes.CONTAINER_CREATE_OK
    }
    
    "delete existing folder if request correct" in new ApiSpecContext {
      val createResp = api create container
      createResp.getStatusCode should be equalTo ApiResponseCodes.CONTAINER_CREATE_OK

      val deleteResp = api delete container
      deleteResp.getStatusCode should be equalTo ApiResponseCodes.CONTAINER_DELETE_OK
    }

    "should return error code if trying to delete non existing folder" in new ApiSpecContext {
      val deleteResp = api delete container

      deleteResp.getStatusCode should be equalTo ApiResponseCodes.CONTAINER_DELETE_NOT_EXIST
    }

    "create new cdn folder if request correct" in new ApiSpecContext(true) {
      val response = api create container
      api delete container

      response.getStatusCode should be equalTo ApiResponseCodes.CONTAINER_CREATE_OK
    }

    "delete existing cdn folder if request correct" in new ApiSpecContext(true) {
      val createResp = api create container
      createResp.getStatusCode should be equalTo ApiResponseCodes.CONTAINER_CREATE_OK

      val deleteResp = api delete container
      deleteResp.getStatusCode should be equalTo ApiResponseCodes.CONTAINER_DELETE_OK
    }

    "should return error code if trying to delete non existing cdn folder" in new ApiSpecContext(true) {
      val deleteResp = api delete container
      deleteResp.getStatusCode should be equalTo ApiResponseCodes.CONTAINER_DELETE_NOT_EXIST
    }

    "receive file info" in new ApiSpecContext {
      val createContainerResponse = api create container
      createContainerResponse.getStatusCode must beEqualTo(ApiResponseCodes.CONTAINER_CREATE_OK)

      val objMetadata = api get RemoteObjectPath(container.name)
      api delete container

      objMetadata must beSome
      objMetadata.get.url.getPath must endWith(container.name)
    }

    "receive None if container not exists" in new ApiSpecContext {
      val folder = api get RemoteObjectPath(containerName)
      folder must beNone
    }
  }

}
