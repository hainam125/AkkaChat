package controllers;

import actors.WebSocketActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import messages.AddUser;
import data.User;
import play.libs.streams.ActorFlow;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class HomeController extends Controller {
    private final ActorSystem system;
    private final Materializer materializer;
    private final ActorRef lobbyActor;

    @Inject
    public HomeController(ActorSystem system, Materializer materializer,@Named("lobbyActor") ActorRef lobbyActor) {
        this.materializer = materializer;
        this.system = system;
        this.lobbyActor = lobbyActor;
    }

    public Result index() {
        String url = routes.HomeController.ws().webSocketURL(request());
        return ok(views.html.index.render(url));
    }

    public WebSocket ws() {
        String username = String.valueOf(request().asScala().id());

        return WebSocket.Json.accept(request ->
                ActorFlow.actorRef(out ->{
                    User user = new User(username, out);
                    AddUser addUser = new AddUser(user);
                    lobbyActor.tell(addUser, ActorRef.noSender());
                    return WebSocketActor.props(user, lobbyActor);
                }, system, materializer)
        );
    }
}
