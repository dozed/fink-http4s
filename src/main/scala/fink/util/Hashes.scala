package fink.util

import java.security.MessageDigest

object Hashes {

  def sha256(s: String): String = {
    val digest = MessageDigest.getInstance("SHA-256")
    val bytes = digest.digest(s.getBytes)
    hex(bytes)
  }

  def md5(s: String): String = {
    val digest = MessageDigest.getInstance("MD5")
    val bytes = digest.digest(s.getBytes)
    hex(bytes)
  }

  val HexChars = "0123456789abcdef".toCharArray

  def hex(bytes: Array[Byte]): String = {
    val buffer = new StringBuilder(bytes.length * 2)
    bytes.foreach { byte =>
      buffer.append(HexChars((byte & 0xF0) >> 4))
      buffer.append(HexChars(byte & 0x0F))
    }
    buffer.toString
  }

}
