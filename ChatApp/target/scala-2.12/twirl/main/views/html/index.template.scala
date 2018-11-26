
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
        <div class="col-xs-7 fill">
            <div>
                <h1 class="text-center" id="room-name">Chat RoomRef</h1>
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
        <div class="col-xs-3">
            <div>
                <h3 class="text-center">RoomRef List</h3>
            </div>
            <div>
                <ul id="rooms" class="list-unstyled">
                </ul>
            </div>

            <div>
                <div>
                    <input id="room" class="form-control" placeholder="RoomRef name" type="text" autofocus/>
                </div>
                <div>
                    <button id="new-room" type="submit"class="form-control btn btn-default">Create RoomRef</button>
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
        var $room           = $("#room");
        var $roomName       = $("#room-name");
        var $newRoomRef        = $("#new-room");
        var $users          = $("#users");
        var connection      = new WebSocket(""""),_display_(/*61.47*/url),format.raw/*61.50*/("""");
        var defaultRoom     = "lobby";
        var currentRoom     = defaultRoom;
        var username        = null;
        var newRoomCmd      = "CmdCreateRoom";
        var joinRoomCmd     = "CmdJoinRoom";
        var chatCmd         = "CmdChat";
        var userActiveCmd   = "CmdUserActive";
        var userInfoCmd     = "CmdUserInfo";
        var privateChatCmd  = "CmdPrivateChat";

        $send.prop("disabled", true);
        var sendMessage = function () """),format.raw/*73.39*/("""{"""),format.raw/*73.40*/("""
            """),format.raw/*74.13*/("""var text = $message.val();
            if(text == "") return;
            $message.val("");
            connection.send(JSON.stringify("""),format.raw/*77.44*/("""{"""),format.raw/*77.45*/("""
                """),format.raw/*78.17*/("""cmd: chatCmd,
                room: currentRoom,
                msg: text
            """),format.raw/*81.13*/("""}"""),format.raw/*81.14*/("""));
        """),format.raw/*82.9*/("""}"""),format.raw/*82.10*/(""";

        var createNewRoom = function () """),format.raw/*84.41*/("""{"""),format.raw/*84.42*/("""
            """),format.raw/*85.13*/("""var roomName = $room.val();
            if(roomName == "") return;
            $room.val("");
            connection.send(JSON.stringify("""),format.raw/*88.44*/("""{"""),format.raw/*88.45*/("""
                """),format.raw/*89.17*/("""cmd: newRoomCmd,
                room: roomName
            """),format.raw/*91.13*/("""}"""),format.raw/*91.14*/("""));
        """),format.raw/*92.9*/("""}"""),format.raw/*92.10*/(""";

        var joinRoom = function(roomName) """),format.raw/*94.43*/("""{"""),format.raw/*94.44*/("""
            """),format.raw/*95.13*/("""connection.send(JSON.stringify("""),format.raw/*95.44*/("""{"""),format.raw/*95.45*/("""
                """),format.raw/*96.17*/("""cmd: joinRoomCmd,
                room: roomName
            """),format.raw/*98.13*/("""}"""),format.raw/*98.14*/("""));
        """),format.raw/*99.9*/("""}"""),format.raw/*99.10*/(""";

        var privateChat = function(roomName)"""),format.raw/*101.45*/("""{"""),format.raw/*101.46*/("""
            """),format.raw/*102.13*/("""connection.send(JSON.stringify("""),format.raw/*102.44*/("""{"""),format.raw/*102.45*/("""
                """),format.raw/*103.17*/("""cmd: privateChatCmd,
                room: roomName
            """),format.raw/*105.13*/("""}"""),format.raw/*105.14*/("""));
        """),format.raw/*106.9*/("""}"""),format.raw/*106.10*/(""";

        connection.onopen = function () """),format.raw/*108.41*/("""{"""),format.raw/*108.42*/("""
            """),format.raw/*109.13*/("""$send.prop("disabled", false);
            $messages.append($("<li class='bg-info' style='font-size: 1.5em'>Connected</li>"));
            $rooms.append($("<li style='font-size: 1.5em'><button data-room='lobby' class='join-room form-control btn btn-warning'>Lobby</button></li>"));
            $send.on("click", sendMessage);
            $newRoomRef.on("click", createNewRoom);
            $message.keypress(function (event) """),format.raw/*114.48*/("""{"""),format.raw/*114.49*/("""
                """),format.raw/*115.17*/("""if(event.keyCode == '13') sendMessage();
            """),format.raw/*116.13*/("""}"""),format.raw/*116.14*/(""");
            $('#rooms').on('click', 'li .join-room', function () """),format.raw/*117.66*/("""{"""),format.raw/*117.67*/("""
                """),format.raw/*118.17*/("""var roomName = $(this).data().room;
                if(roomName != currentRoom) joinRoom(roomName);
            """),format.raw/*120.13*/("""}"""),format.raw/*120.14*/(""");
            $('#users').on('click', 'li .private-chat', function () """),format.raw/*121.69*/("""{"""),format.raw/*121.70*/("""
                """),format.raw/*122.17*/("""var toUser = $(this).data().userRef;
                if(username != toUser) """),format.raw/*123.40*/("""{"""),format.raw/*123.41*/("""
                    """),format.raw/*124.21*/("""var newRoomName = username > toUser ? username + "-" + toUser : toUser + "-" + username;
                    if(newRoomName != currentRoom)"""),format.raw/*125.51*/("""{"""),format.raw/*125.52*/("""
                        """),format.raw/*126.25*/("""currentRoom = newRoomName;
                        privateChat(currentRoom);
                    """),format.raw/*128.21*/("""}"""),format.raw/*128.22*/("""
                """),format.raw/*129.17*/("""}"""),format.raw/*129.18*/("""
            """),format.raw/*130.13*/("""}"""),format.raw/*130.14*/(""");
        """),format.raw/*131.9*/("""}"""),format.raw/*131.10*/(""";

        connection.onerror = function (err) """),format.raw/*133.45*/("""{"""),format.raw/*133.46*/("""
            """),format.raw/*134.13*/("""console.log("WebSocket Error ", err);
        """),format.raw/*135.9*/("""}"""),format.raw/*135.10*/(""";

        connection.onmessage = function (event) """),format.raw/*137.49*/("""{"""),format.raw/*137.50*/("""
            """),format.raw/*138.13*/("""console.log(event.data)
            var command = JSON.parse(event.data);
            if(command.cmd == chatCmd) """),format.raw/*140.40*/("""{"""),format.raw/*140.41*/("""
                """),format.raw/*141.17*/("""$messages.append($("<li style='font-size: 1.5em'>" + command.msg + "</li>"));
            """),format.raw/*142.13*/("""}"""),format.raw/*142.14*/("""
            """),format.raw/*143.13*/("""else if(command.cmd == newRoomCmd) """),format.raw/*143.48*/("""{"""),format.raw/*143.49*/("""
                """),format.raw/*144.17*/("""$rooms.empty();
                for (var i = 0; i < command.rooms.length; i++) """),format.raw/*145.64*/("""{"""),format.raw/*145.65*/("""
                    """),format.raw/*146.21*/("""$rooms.append($("<li style='font-size: 1.5em'><button data-room='" + command.rooms[i] + "' class='join-room form-control btn btn-warning'>" + command.rooms[i] + "</button></li>"));
                """),format.raw/*147.17*/("""}"""),format.raw/*147.18*/("""
            """),format.raw/*148.13*/("""}"""),format.raw/*148.14*/("""
            """),format.raw/*149.13*/("""else if(command.cmd == joinRoomCmd) """),format.raw/*149.49*/("""{"""),format.raw/*149.50*/("""
                """),format.raw/*150.17*/("""currentRoom = command.room;
                $roomName.html(currentRoom);
                $messages.empty();
                for (var i = 0; i < command.history.length; i++) """),format.raw/*153.66*/("""{"""),format.raw/*153.67*/("""
                    """),format.raw/*154.21*/("""$messages.append($("<li style='font-size: 1.5em'>" + command.history[i] + "</li>"));
                """),format.raw/*155.17*/("""}"""),format.raw/*155.18*/("""
            """),format.raw/*156.13*/("""}"""),format.raw/*156.14*/("""
            """),format.raw/*157.13*/("""else if(command.cmd == userActiveCmd) """),format.raw/*157.51*/("""{"""),format.raw/*157.52*/("""
                """),format.raw/*158.17*/("""var users = command.users;
                users.forEach(function (userRef, i) """),format.raw/*159.53*/("""{"""),format.raw/*159.54*/("""
                    """),format.raw/*160.21*/("""var cssClass = userRef == username ? "btn-primary" : "btn-danger";
                    if(command.online) $users.append($("<li style='font-size: 1.2em' class='" + userRef + "'><button data-userRef='" + userRef + "' class='form-control private-chat btn " + cssClass + "'>" + userRef + "</button></li>"));
                    else $users.find($('.' + userRef)).remove();
                """),format.raw/*163.17*/("""}"""),format.raw/*163.18*/(""");
            """),format.raw/*164.13*/("""}"""),format.raw/*164.14*/("""
            """),format.raw/*165.13*/("""else if(command.cmd == userInfoCmd) """),format.raw/*165.49*/("""{"""),format.raw/*165.50*/("""
                """),format.raw/*166.17*/("""username = command.username;
            """),format.raw/*167.13*/("""}"""),format.raw/*167.14*/("""
        """),format.raw/*168.9*/("""}"""),format.raw/*168.10*/(""";
    </script>
""")))}),format.raw/*170.2*/("""
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
                  DATE: Mon Nov 26 13:32:01 ICT 2018
                  SOURCE: D:/Docker/ChatApp/ChatApp/app/views/index.scala.html
                  HASH: 529c88c7293ab3aade2505b18e871375c6cc00e9
                  MATRIX: 948->1|1056->14|1086->19|1117->42|1156->44|1188->50|3270->2105|3294->2108|3806->2592|3835->2593|3877->2607|4043->2745|4072->2746|4118->2764|4236->2854|4265->2855|4305->2868|4334->2869|4407->2914|4436->2915|4478->2929|4646->3069|4675->3070|4721->3088|4811->3150|4840->3151|4880->3164|4909->3165|4984->3212|5013->3213|5055->3227|5114->3258|5143->3259|5189->3277|5280->3340|5309->3341|5349->3354|5378->3355|5456->3404|5486->3405|5529->3419|5589->3450|5619->3451|5666->3469|5761->3535|5791->3536|5832->3549|5862->3550|5936->3595|5966->3596|6009->3610|6468->4040|6498->4041|6545->4059|6628->4113|6658->4114|6756->4183|6786->4184|6833->4202|6976->4316|7006->4317|7107->4389|7137->4390|7184->4408|7290->4485|7320->4486|7371->4508|7540->4648|7570->4649|7625->4675|7753->4774|7783->4775|7830->4793|7860->4794|7903->4808|7933->4809|7973->4821|8003->4822|8081->4871|8111->4872|8154->4886|8229->4933|8259->4934|8341->4987|8371->4988|8414->5002|8558->5117|8588->5118|8635->5136|8755->5227|8785->5228|8828->5242|8892->5277|8922->5278|8969->5296|9078->5376|9108->5377|9159->5399|9386->5597|9416->5598|9459->5612|9489->5613|9532->5627|9597->5663|9627->5664|9674->5682|9879->5858|9909->5859|9960->5881|10091->5983|10121->5984|10164->5998|10194->5999|10237->6013|10304->6051|10334->6052|10381->6070|10490->6150|10520->6151|10571->6173|10988->6561|11018->6562|11063->6578|11093->6579|11136->6593|11201->6629|11231->6630|11278->6648|11349->6690|11379->6691|11417->6701|11447->6702|11497->6721
                  LINES: 28->1|33->1|35->3|35->3|35->3|36->4|93->61|93->61|105->73|105->73|106->74|109->77|109->77|110->78|113->81|113->81|114->82|114->82|116->84|116->84|117->85|120->88|120->88|121->89|123->91|123->91|124->92|124->92|126->94|126->94|127->95|127->95|127->95|128->96|130->98|130->98|131->99|131->99|133->101|133->101|134->102|134->102|134->102|135->103|137->105|137->105|138->106|138->106|140->108|140->108|141->109|146->114|146->114|147->115|148->116|148->116|149->117|149->117|150->118|152->120|152->120|153->121|153->121|154->122|155->123|155->123|156->124|157->125|157->125|158->126|160->128|160->128|161->129|161->129|162->130|162->130|163->131|163->131|165->133|165->133|166->134|167->135|167->135|169->137|169->137|170->138|172->140|172->140|173->141|174->142|174->142|175->143|175->143|175->143|176->144|177->145|177->145|178->146|179->147|179->147|180->148|180->148|181->149|181->149|181->149|182->150|185->153|185->153|186->154|187->155|187->155|188->156|188->156|189->157|189->157|189->157|190->158|191->159|191->159|192->160|195->163|195->163|196->164|196->164|197->165|197->165|197->165|198->166|199->167|199->167|200->168|200->168|202->170
                  -- GENERATED --
              */
          