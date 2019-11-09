package fink.media

import java.io.File

import fink.data.AppConfig

object StaticFiles {

  def mkPublicFile(appConfig: AppConfig, fileName: String): File = {
    new File(s"${appConfig.publicDirectory}/$fileName")
  }

  def mkUploadFile(appConfig: AppConfig, fileName: String): File = {
    new File(s"${appConfig.uploadDirectory}/$fileName")
  }

  def mkDataFile(appConfig: AppConfig, fileName: String): File = {
    new File(s"${appConfig.dataDirectory}/$fileName")
  }


}
