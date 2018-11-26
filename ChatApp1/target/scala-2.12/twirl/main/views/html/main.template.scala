
package views.html

import _root_.play.twirl.api.TwirlFeatureImports._
import _root_.play.twirl.api.TwirlHelperImports._
import _root_.play.twirl.api.Html
import _root_.play.twirl.api.JavaScript
import _root_.play.twirl.api.Txt
import _root_.play.twirl.api.Xml
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import java.lang._
import java.util._
import scala.collection.JavaConverters._
import play.core.j.PlayMagicForJava._
import play.mvc._
import play.api.data.Field
import play.mvc.Http.Context.Implicit._
import play.data._
import play.core.j.PlayFormsMagicForJava._

object main extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template2[String,Html,play.twirl.api.HtmlFormat.Appendable] {

  /*
 * This template is called from the `index` template. This template
 * handles the rendering of the page header and body tags. It takes
 * two arguments, a `String` for the title of the page and an `Html`
 * object to insert into the body of the page.
 */
  def apply/*7.2*/(title: String)(content: Html):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*7.32*/("""

"""),format.raw/*9.1*/("""<!DOCTYPE html>
<html lang="en">
    <head>
        <title>"""),_display_(/*12.17*/title),format.raw/*12.22*/("""</title>
        <link rel="stylesheet" media="screen" href=""""),_display_(/*13.54*/routes/*13.60*/.Assets.versioned("stylesheets/main.css")),format.raw/*13.101*/("""">
        <link rel="stylesheet" media="screen" href=""""),_display_(/*14.54*/routes/*14.60*/.Assets.versioned("bootstrap/css/bootstrap.min.css")),format.raw/*14.112*/("""">
        <link rel="shortcut icon" type="image/png" href=""""),_display_(/*15.59*/routes/*15.65*/.Assets.versioned("images/favicon.png")),format.raw/*15.104*/("""">
        <script src=""""),_display_(/*16.23*/routes/*16.29*/.Assets.versioned("javascripts/jquery-3.3.1.min.js")),format.raw/*16.81*/("""" type="text/javascript"></script>
        <script src=""""),_display_(/*17.23*/routes/*17.29*/.Assets.versioned("javascripts/index.js")),format.raw/*17.70*/("""" type="text/javascript"></script>
    </head>
    <body>
        <div class="container fill">
        """),_display_(/*21.10*/content),format.raw/*21.17*/("""
        """),format.raw/*22.9*/("""</div>
    </body>
</html>
"""))
      }
    }
  }

  def render(title:String,content:Html): play.twirl.api.HtmlFormat.Appendable = apply(title)(content)

  def f:((String) => (Html) => play.twirl.api.HtmlFormat.Appendable) = (title) => (content) => apply(title)(content)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: Mon Nov 26 08:08:02 ICT 2018
                  SOURCE: D:/Docker/ChatApp/ChatApp/app/views/main.scala.html
                  HASH: bc227bf72f22bad77276569af01719d520cf2340
                  MATRIX: 1211->266|1336->296|1366->300|1456->363|1482->368|1572->431|1587->437|1650->478|1734->535|1749->541|1823->593|1912->655|1927->661|1988->700|2041->726|2056->732|2129->784|2214->842|2229->848|2291->889|2426->997|2454->1004|2491->1014
                  LINES: 33->7|38->7|40->9|43->12|43->12|44->13|44->13|44->13|45->14|45->14|45->14|46->15|46->15|46->15|47->16|47->16|47->16|48->17|48->17|48->17|52->21|52->21|53->22
                  -- GENERATED --
              */
          