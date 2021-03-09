#!/usr/bin/env bash

java -Xmx1000m \
  -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 \
  -Dlogback.configurationFile=conf/logback.xml \
  -Dconfig.file=conf/application.conf \
  -jar backend.jar fink.Http4sLauncher
