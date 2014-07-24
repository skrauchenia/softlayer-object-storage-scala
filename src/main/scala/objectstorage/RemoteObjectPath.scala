package objectstorage

/**
 *
 * @author Sergey Krauchenia
 */
trait RemoteObjectPath {
  def name: String
  def remotePath: Seq[String]

  def constructObjectPath: String = {
    val pathParts = remotePath :+ name
    pathParts.mkString("/")
  }

  def constructObjectUrl(implicit authorizedConnection: Authorized): String = s"${authorizedConnection.storageUrl}/$constructObjectPath"
}

object RemoteObjectPath {
  def apply(remoteName: String, remotePathParts: String*): RemoteObjectPath = {
    new RemoteObjectPath {
      val remotePath = remotePathParts
      val name = remoteName
    }
  }
}

case class NewRemoteFile(name: String, localFilePath: String, remotePath: String*) extends RemoteObjectPath
case class NewRemoteFolder(name: String, cdn: Boolean, remotePath: String*) extends RemoteObjectPath
