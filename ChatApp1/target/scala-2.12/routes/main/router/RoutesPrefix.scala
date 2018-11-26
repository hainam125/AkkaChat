// @GENERATOR:play-routes-compiler
// @SOURCE:D:/Docker/ChatApp/ChatApp1/conf/routes
// @DATE:Mon Nov 26 16:39:25 ICT 2018


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
