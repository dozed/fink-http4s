package fink

import org.mindrot.jbcrypt.BCrypt

package object data {


  type UnixTime = Long
  type UserId = Long

  def mkTime: UnixTime = System.currentTimeMillis()

  def mkPassword(str: String): String = BCrypt.hashpw(str, BCrypt.gensalt())


}
