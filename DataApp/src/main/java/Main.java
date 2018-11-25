import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main {
  public static void main(String[] args) {
    final ActorSystem system = ActorSystem.create("akka");
    system.actorOf(Props.create(AkkaDb.class), "akka-db");
  }
}
