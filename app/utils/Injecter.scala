package utils

import com.google.inject.Guice
import modules.MyModule

trait Injecter {
  val injecter = Guice.createInjector(new MyModule)
}
