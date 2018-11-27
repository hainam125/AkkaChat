
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

object index extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template1[String,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(url: String):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.15*/("""

"""),_display_(/*3.2*/main("Welcome to Chat")/*3.25*/ {_display_(Seq[Any](format.raw/*3.27*/("""
    """),format.raw/*4.5*/("""<div class="row fill">
        <div class="col-xs-10 fill">
            <div>
                <h1 class="text-center" id="room-name">Chat Room</h1>
            </div>
            <div>
                <ul id="messages" class="list-unstyled">
                </ul>
            </div>

            <div class="footer">
                <div class="col-xs-8 col-sm-9">
                    <input id="message" class="form-control" placeholder="Enter Message" type="text" autofocus/>
                </div>
                <div class="col-xs-4 col-sm-3">
                    <button id="send" type="submit"class="form-control">Send</button>
                </div>
            </div>
        </div>
        <div class="col-xs-2">
            <div>
                <h3 class="text-center">Users</h3>
            </div>
            <div>
                <ul id="users" class="list-unstyled">
                </ul>
            </div>
        </div>
    </div>

    <script language="javascript">
        var $messages       = $("#messages");
        var $message        = $("#message");
        var $send           = $("#send");
        var $rooms          = $("#rooms");
        var $users          = $("#users");
        var connection      = new WebSocket(""""),_display_(/*40.47*/url),format.raw/*40.50*/("""");
        var defaultRoom     = "lobby";
        var currentRoom     = defaultRoom;
        var username        = null;
        var chatCmd         = "CmdChat";
        var joinRoomCmd     = "CmdJoinRoom";
        var userActiveCmd   = "CmdUserActive";
        var userInfoCmd     = "CmdUserInfo";

        $send.prop("disabled", true);
        var sendMessage = function () """),format.raw/*50.39*/("""{"""),format.raw/*50.40*/("""
            """),format.raw/*51.13*/("""var text = $message.val();
            if(text == "") return;
            $message.val("");
            connection.send(JSON.stringify("""),format.raw/*54.44*/("""{"""),format.raw/*54.45*/("""
                """),format.raw/*55.17*/("""cmd: chatCmd,
                room: currentRoom,
                msg: text
            """),format.raw/*58.13*/("""}"""),format.raw/*58.14*/("""));
        """),format.raw/*59.9*/("""}"""),format.raw/*59.10*/(""";

        connection.onopen = function () """),format.raw/*61.41*/("""{"""),format.raw/*61.42*/("""
            """),format.raw/*62.13*/("""$send.prop("disabled", false);
            $messages.append($("<li class='bg-info' style='font-size: 1.5em'>Connected</li>"));
            $rooms.append($("<li style='font-size: 1.5em'><button data-room='lobby' class='join-room form-control btn btn-warning'>Lobby</button></li>"));
            $send.on("click", sendMessage);
            $message.keypress(function (event) """),format.raw/*66.48*/("""{"""),format.raw/*66.49*/("""
                """),format.raw/*67.17*/("""if(event.keyCode == '13') sendMessage();
            """),format.raw/*68.13*/("""}"""),format.raw/*68.14*/(""");
        """),format.raw/*69.9*/("""}"""),format.raw/*69.10*/(""";

        connection.onerror = function (err) """),format.raw/*71.45*/("""{"""),format.raw/*71.46*/("""
            """),format.raw/*72.13*/("""console.log("WebSocket Error ", err);
        """),format.raw/*73.9*/("""}"""),format.raw/*73.10*/(""";

        connection.onmessage = function (event) """),format.raw/*75.49*/("""{"""),format.raw/*75.50*/("""
            """),format.raw/*76.13*/("""console.log(event.data)
            var command = JSON.parse(event.data);
            if(command.cmd == chatCmd) """),format.raw/*78.40*/("""{"""),format.raw/*78.41*/("""
                """),format.raw/*79.17*/("""$messages.append($("<li style='font-size: 1.5em'>" + command.msg + "</li>"));
            """),format.raw/*80.13*/("""}"""),format.raw/*80.14*/("""
            """),format.raw/*81.13*/("""else if(command.cmd == userActiveCmd) """),format.raw/*81.51*/("""{"""),format.raw/*81.52*/("""
                """),format.raw/*82.17*/("""var users = command.users;
                users.forEach(function (userRef, i) """),format.raw/*83.53*/("""{"""),format.raw/*83.54*/("""
                    """),format.raw/*84.21*/("""var cssClass = userRef == username ? "btn-primary" : "btn-danger";
                    if(command.online) $users.append($("<li style='font-size: 1.2em' class='" + userRef + "'><button data-user='" + userRef + "' class='form-control private-chat btn " + cssClass + "'>" + userRef + "</button></li>"));
                    else $users.find($('.' + userRef)).remove();
                """),format.raw/*87.17*/("""}"""),format.raw/*87.18*/(""");
            """),format.raw/*88.13*/("""}"""),format.raw/*88.14*/("""
            """),format.raw/*89.13*/("""else if(command.cmd == joinRoomCmd) """),format.raw/*89.49*/("""{"""),format.raw/*89.50*/("""
                """),format.raw/*90.17*/("""$messages.empty();
                for (var i = 0; i < command.history.length; i++) """),format.raw/*91.66*/("""{"""),format.raw/*91.67*/("""
                    """),format.raw/*92.21*/("""$messages.append($("<li style='font-size: 1.5em'>" + command.history[i] + "</li>"));
                """),format.raw/*93.17*/("""}"""),format.raw/*93.18*/("""
            """),format.raw/*94.13*/("""}"""),format.raw/*94.14*/("""
            """),format.raw/*95.13*/("""else if(command.cmd == userInfoCmd) """),format.raw/*95.49*/("""{"""),format.raw/*95.50*/("""
                """),format.raw/*96.17*/("""username = command.username;
            """),format.raw/*97.13*/("""}"""),format.raw/*97.14*/("""
        """),format.raw/*98.9*/("""}"""),format.raw/*98.10*/(""";
    </script>
""")))}),format.raw/*100.2*/("""
"""))
      }
    }
  }

  def render(url:String): play.twirl.api.HtmlFormat.Appendable = apply(url)

  def f:((String) => play.twirl.api.HtmlFormat.Appendable) = (url) => apply(url)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: Tue Nov 27 16:35:23 ICT 2018
                  SOURCE: D:/Docker/ChatApp/ChatApp/app/views/index.scala.html
                  HASH: a36f05a67f81df829a3e6825169c0b9fdbe6a1bd
                  MATRIX: 948->1|1056->14|1086->19|1117->42|1156->44|1188->50|2502->1337|2526->1340|2941->1727|2970->1728|3012->1742|3178->1880|3207->1881|3253->1899|3371->1989|3400->1990|3440->2003|3469->2004|3542->2049|3571->2050|3613->2064|4018->2441|4047->2442|4093->2460|4175->2514|4204->2515|4243->2527|4272->2528|4349->2577|4378->2578|4420->2592|4494->2639|4523->2640|4604->2693|4633->2694|4675->2708|4818->2823|4847->2824|4893->2842|5012->2933|5041->2934|5083->2948|5149->2986|5178->2987|5224->3005|5332->3085|5361->3086|5411->3108|5824->3493|5853->3494|5897->3510|5926->3511|5968->3525|6032->3561|6061->3562|6107->3580|6220->3665|6249->3666|6299->3688|6429->3790|6458->3791|6500->3805|6529->3806|6571->3820|6635->3856|6664->3857|6710->3875|6780->3917|6809->3918|6846->3928|6875->3929|6925->3948
                  LINES: 28->1|33->1|35->3|35->3|35->3|36->4|72->40|72->40|82->50|82->50|83->51|86->54|86->54|87->55|90->58|90->58|91->59|91->59|93->61|93->61|94->62|98->66|98->66|99->67|100->68|100->68|101->69|101->69|103->71|103->71|104->72|105->73|105->73|107->75|107->75|108->76|110->78|110->78|111->79|112->80|112->80|113->81|113->81|113->81|114->82|115->83|115->83|116->84|119->87|119->87|120->88|120->88|121->89|121->89|121->89|122->90|123->91|123->91|124->92|125->93|125->93|126->94|126->94|127->95|127->95|127->95|128->96|129->97|129->97|130->98|130->98|132->100
                  -- GENERATED --
              */
          