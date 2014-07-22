package objectstorage

import java.util.UUID

/**
 *
 * @author Sergey Krauchenia
 */
class StorageObjectActionsTest extends BaseSpec {

  "Storage object" should {
    "upload new file if request correct" in {
      val (api, container) = createContainerAndApi()
      val response = api create container
      response.getStatusCode should be equalTo ApiResponseCodes.CONTAINER_CREATE_OK
      val obj = createObject(container)
      val fileResp = api create obj
      api delete obj
      api delete container
      val expectedMd5: String = Helpers.md5(obj.filePath)
      val uploadedFileMd5: String = fileResp.getHeader(ApiHeaders.UPLOADED_FILE_MD5)

      expectedMd5 must be equalTo uploadedFileMd5
      fileResp.getStatusCode should be equalTo ApiResponseCodes.OBJECT_CREATE_OK
    }
  }

  def createContainerAndApi(cdn: Boolean = false) = {
    val containerName = UUID.randomUUID().toString
    val connection = Connection(userName, apiKey)
    val api = Api(connection).blocking
    val container = StorageContainer(containerName)

    (api, container)
  }

  def createObject(container: StorageContainer) = {
    val filePath = this.getClass.getClassLoader.getResource("test.data").getPath
    StorageObject("test.data", filePath, container)
  }

}
