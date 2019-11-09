package fink.media

import java.security.MessageDigest
import cats.syntax.all._

object Encoding {

  def fooId: String = {
    val x = scala.util.Random.nextDouble
    val l = (java.lang.Long.MAX_VALUE * x).toLong
    dec2base(l)
  }

  def encode(table: IndexedSeq[Char])(x: BigInt): String = {
    val base = table.size
    def encode0(x: BigInt, s: String = ""): String = {
      val xp = x / base
      val r = (x % base).toInt
      val c = table(r)
      if (xp > 0) encode0(xp, c + s) else c + s
    }
    encode0(x)
  }

  def decode(table: String)(s: String): BigInt = {
    val base = table.size
    s.toList.map(table.indexOf(_)).map(BigInt(_)).reduceLeft( _ * base + _)
  }

  val encodeTable: IndexedSeq[Char] = ('A' to 'Z') ++ ('a' to 'z') ++ ('0' to '9')

  val hex2dec = decode("0123456789abcdef") _
  val bin2dec = decode("01") _
  val base2dec = decode(encodeTable.mkString) _

  val dec2hex = encode(('0' to '9') ++ ('a' to 'f')) _
  val dec2bin = encode(('0' to '1')) _
  val dec2base = encode(encodeTable) _


  def fromBase64(str: String): Either[Throwable, String] = {
    Either.catchNonFatal(java.util.Base64.getDecoder.decode(str))
      .map(a => new String(a))
  }

}

object Hashes {
  val shaTl = new ThreadLocal[MessageDigest]()
  val mdTl = new ThreadLocal[MessageDigest]()

  def sha = {
    if (shaTl.get == null) {
      shaTl.set(MessageDigest.getInstance("SHA-256"))
    }
    shaTl.get
  }


  def md = {
    if (mdTl.get == null) {
      mdTl.set(MessageDigest.getInstance("MD5"))
    }
    mdTl.get
  }

  def sha256(s: String): String = {
    sha.digest(s.getBytes)
      .foldLeft("")((s: String, b: Byte) => s +
      Character.forDigit((b & 0xf0) >> 4, 16) +
      Character.forDigit(b & 0x0f, 16))
  }

  def md5(s: String): String = {
    return md.digest(s.getBytes("UTF-8"))
      .foldLeft("")((s: String, b: Byte) => s +
      Character.forDigit((b & 0xf0) >> 4, 16) +
      Character.forDigit(b & 0x0f, 16))
  }
}

// source: https://github.com/marklister/base64
object Base64 {

  class B64Scheme(val encodeTable: IndexedSeq[Char]) {
    lazy val decodeTable = collection.immutable.TreeMap(encodeTable.zipWithIndex: _*)
  }

  lazy val base64 = new B64Scheme(('A' to 'Z') ++ ('a' to 'z') ++ ('0' to '9') ++ Seq('+', '/'))
  lazy val base64Url = new B64Scheme(base64.encodeTable.dropRight(2) ++ Seq('-', '_'))

  implicit class  Encoder(b: Array[Byte]) {
    private[this] val zero = Array(0, 0).map(_.toByte)
    lazy val pad = (3 - b.length % 3) % 3

    def toBase64(implicit scheme: B64Scheme = base64): String = {
      def sixBits(x: Array[Byte]): Seq[Int] = {
        val a = (x(0) & 0xfc) >> 2
        val b = ((x(0) & 0x3) << 4) + ((x(1) & 0xf0) >> 4)
        val c = ((x(1) & 0xf) << 2) + ((x(2) & 0xc0) >> 6)
        val d = (x(2)) & 0x3f
        Seq(a, b, c, d)
      }
      ((b ++ zero.take(pad)).grouped(3)
        .flatMap(sixBits(_))
        .map(x => scheme.encodeTable(x))
        .toSeq
        .dropRight(pad) :+ "=" * pad)
        .mkString
    }
  }

  implicit class Decoder(s: String) {
    lazy val cleanS = s.reverse.dropWhile(_ == '=').reverse
    lazy val pad = s.length - cleanS.length

    def toByteArray(implicit scheme: B64Scheme = base64): Array[Byte] = {
      def threeBytes(s: Seq[Char]): Array[Byte] = {
        val r = s.map(scheme.decodeTable(_)).foldLeft(0)((a,b)=>(a << 6) +b)
        java.nio.ByteBuffer.allocate(8).putLong(r).array().takeRight(3)
      }
      if (pad > 2 || s.length % 4 != 0) throw new java.lang.IllegalArgumentException("Invalid Base64 String:" + s)
      if (!cleanS.forall(scheme.encodeTable.contains(_))) throw new java.lang.IllegalArgumentException("Invalid Base64 String:" + s)

      (cleanS + "A" * pad)
        .grouped(4)
        .map(threeBytes(_))
        .flatten
        .toArray
        .dropRight(pad)
    }
  }

}

