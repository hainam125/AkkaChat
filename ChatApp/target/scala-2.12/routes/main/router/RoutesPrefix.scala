// @GENERATOR:play-routes-compiler
// @SOURCE:D:/Docker/ChatApp/ChatApp/conf/routes
// @DATE:Tue Nov 27 16:51:01 ICT 2018


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
