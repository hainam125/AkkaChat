
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
        <div class="col-xs-3">
            <div>
                <h3 class="text-center">Room List</h3>
            </div>
            <div>
                <ul id="rooms" class="list-unstyled">
                </ul>
            </div>

            <div>
                <div>
                    <input id="room" class="form-control" placeholder="Room name" type="text" autofocus/>
                </div>
                <div>
                    <button id="new-room" type="submit"class="form-control btn btn-default">Create Room</button>
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
                """),format.raw/*122.17*/("""var toUser = $(this).data().user;
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
                    if(command.online) $users.append($("<li style='font-size: 1.2em' class='" + userRef + "'><button data-user='" + userRef + "' class='form-control private-chat btn " + cssClass + "'>" + userRef + "</button></li>"));
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
                  DATE: Mon Nov 26 16:37:17 ICT 2018
                  SOURCE: D:/Docker/ChatApp/ChatApp/app/views/index.scala.html
                  HASH: 8ddbbe749558e3cdd2ce755957cce9a5fe3c307c
                  MATRIX: 948->1|1056->14|1086->19|1117->42|1156->44|1188->50|3258->2093|3282->2096|3794->2580|3823->2581|3865->2595|4031->2733|4060->2734|4106->2752|4224->2842|4253->2843|4293->2856|4322->2857|4395->2902|4424->2903|4466->2917|4634->3057|4663->3058|4709->3076|4799->3138|4828->3139|4868->3152|4897->3153|4972->3200|5001->3201|5043->3215|5102->3246|5131->3247|5177->3265|5268->3328|5297->3329|5337->3342|5366->3343|5444->3392|5474->3393|5517->3407|5577->3438|5607->3439|5654->3457|5749->3523|5779->3524|5820->3537|5850->3538|5924->3583|5954->3584|5997->3598|6456->4028|6486->4029|6533->4047|6616->4101|6646->4102|6744->4171|6774->4172|6821->4190|6964->4304|6994->4305|7095->4377|7125->4378|7172->4396|7275->4470|7305->4471|7356->4493|7525->4633|7555->4634|7610->4660|7738->4759|7768->4760|7815->4778|7845->4779|7888->4793|7918->4794|7958->4806|7988->4807|8066->4856|8096->4857|8139->4871|8214->4918|8244->4919|8326->4972|8356->4973|8399->4987|8543->5102|8573->5103|8620->5121|8740->5212|8770->5213|8813->5227|8877->5262|8907->5263|8954->5281|9063->5361|9093->5362|9144->5384|9371->5582|9401->5583|9444->5597|9474->5598|9517->5612|9582->5648|9612->5649|9659->5667|9864->5843|9894->5844|9945->5866|10076->5968|10106->5969|10149->5983|10179->5984|10222->5998|10289->6036|10319->6037|10366->6055|10475->6135|10505->6136|10556->6158|10970->6543|11000->6544|11045->6560|11075->6561|11118->6575|11183->6611|11213->6612|11260->6630|11331->6672|11361->6673|11399->6683|11429->6684|11479->6703
                  LINES: 28->1|33->1|35->3|35->3|35->3|36->4|93->61|93->61|105->73|105->73|106->74|109->77|109->77|110->78|113->81|113->81|114->82|114->82|116->84|116->84|117->85|120->88|120->88|121->89|123->91|123->91|124->92|124->92|126->94|126->94|127->95|127->95|127->95|128->96|130->98|130->98|131->99|131->99|133->101|133->101|134->102|134->102|134->102|135->103|137->105|137->105|138->106|138->106|140->108|140->108|141->109|146->114|146->114|147->115|148->116|148->116|149->117|149->117|150->118|152->120|152->120|153->121|153->121|154->122|155->123|155->123|156->124|157->125|157->125|158->126|160->128|160->128|161->129|161->129|162->130|162->130|163->131|163->131|165->133|165->133|166->134|167->135|167->135|169->137|169->137|170->138|172->140|172->140|173->141|174->142|174->142|175->143|175->143|175->143|176->144|177->145|177->145|178->146|179->147|179->147|180->148|180->148|181->149|181->149|181->149|182->150|185->153|185->153|186->154|187->155|187->155|188->156|188->156|189->157|189->157|189->157|190->158|191->159|191->159|192->160|195->163|195->163|196->164|196->164|197->165|197->165|197->165|198->166|199->167|199->167|200->168|200->168|202->170
                  -- GENERATED --
              */
          