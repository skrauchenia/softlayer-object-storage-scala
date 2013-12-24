package objectstorage

import java.security.MessageDigest
import java.nio.file.{Paths, Files}

/**
 *
 * @author Sergey Krauchenia
 */
object Helpers {

  def md5(filePath: String) : String = {
    val md5 = MessageDigest.getInstance("MD5")
    md5.reset()
    md5.update(Files.readAllBytes(Paths.get(filePath)))

    md5.digest().map(0xFF & _).map { "%02x".format(_) }.foldLeft(""){_ + _}
  }
}
