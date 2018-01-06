package utils

import javax.inject.Inject

import com.typesafe.config
import play.api.Configuration

class MyConfig @Inject()(override val underlying: config.Config) extends Configuration(underlying) {

}
