package hello

import org.springframework.context.annotation.{Configuration, ComponentScan}
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, ResponseBody}
import org.springframework.web.bind.annotation.RequestParam
/**
 * This config class will trigger Spring @annotation scanning and auto configure Spring context.
 *
 * @author Adwait
 * @since 1.0
 */
@Controller
@Configuration
@EnableAutoConfiguration
@ComponentScan
class HelloConfig {

  @RequestMapping(Array("/"))
  @ResponseBody
  def displayHelloWorld(): String = "Hello World!"
 
}