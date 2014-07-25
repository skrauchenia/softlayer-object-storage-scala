package objectstorage

/**
 *
 * @author Sergey Krauchenia
 */
class StorageObjectActionsTest extends BaseSpec {

  "Storage object api" should {
    "upload new file if request correct" in new ApiSpecContext {
      val response = api create container
      response.getStatusCode should be equalTo ApiResponseCodes.CONTAINER_CREATE_OK

      val obj = createObject(container)
      val fileResp = api create obj
      api delete obj
      api delete container
      val expectedMd5: String = Helpers.md5(obj.localFilePath)
      val uploadedFileMd5: String = fileResp.getHeader(ApiHeaders.UPLOADED_FILE_MD5)

      expectedMd5 must be equalTo uploadedFileMd5
      fileResp.getStatusCode should be equalTo ApiResponseCodes.OBJECT_CREATE_OK
    }

    "get remote object metadata" in new ApiSpecContext {
      api create container
      val obj = createObject(container)
      api create obj

      val metadata = api getMetadata obj

      api delete obj
      api delete container

      metadata must beSome
      metadata.get.url.toString must endWith(obj.name)
    }

    "return None if acquiring file not exist" in new ApiSpecContext {
      val metadata = api getMetadata RemoteObjectPath("not-exists")
      metadata must beNone
    }

    "delete existing file" in new ApiSpecContext {
      api create container
      val obj = createObject(container)
      api create obj

      val metadata = api getMetadata obj
      metadata must beSome

      api delete obj
      val notExistingObject = api getMetadata obj
      api delete container

      notExistingObject must beNone
    }
  }

  def createObject(container: NewRemoteFolder) = {
    val filePath = this.getClass.getClassLoader.getResource("test.data").getPath
    NewRemoteFile("test.data", filePath, container.name)
  }

}
