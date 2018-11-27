// @GENERATOR:play-routes-compiler
// @SOURCE:D:/Docker/ChatApp/ChatApp1/conf/routes
// @DATE:Tue Nov 27 16:48:08 ICT 2018


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
