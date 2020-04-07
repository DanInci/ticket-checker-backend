package ticheck

import ticheck.effect._

import org.http4s.Header

/**
  *
  * @author Daniel Incicau, https://github.com/DanInci
  * @since 3/30/2020
  *
  */
package object http extends Http4sCirceInstances with RoutesHelpers {

  object Http4sEC extends PhantomType[ExecutionContext] {
    def safe(ec: ExecutionContextFT): this.Type = this.apply(ec)
  }
  type Http4sEC = Http4sEC.Type

  lazy val cors = List(
    Header("Access-Control-Allow-Origin", "*"),
    Header("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS"),
    Header("Access-Control-Allow-Headers", "Origin, Content-Type, X-Auth-Token"),
  )

}
