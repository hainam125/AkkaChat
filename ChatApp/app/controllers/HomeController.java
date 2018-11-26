package controllers;

import actors.WebSocketActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import messages.AddUser;
import models.UserRef;
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
    public HomeController(ActorSystem system, Materializer materializer,@Named("chatActor") ActorRef chatActor) {
        this.materializer = materializer;
        this.system = system;
        this.lobbyActor = chatActor;
    }

    public Result index() {
        String url = routes.HomeController.ws().webSocketURL(request());
        return ok(views.html.index.render(url));
    }

    public WebSocket ws() {
        long id = request().asScala().id();

        return WebSocket.Json.accept(request ->
                ActorFlow.actorRef(out ->{
                    UserRef userRef = new UserRef(out, id, "127.0.0.1:3001");
                    AddUser addUser = new AddUser(userRef);
                    lobbyActor.tell(addUser, ActorRef.noSender());
                    return WebSocketActor.props(userRef, lobbyActor);
                }, system, materializer)
        );
    }
}
