package modules;

import actors.ChatActor;
import com.google.inject.AbstractModule;
import play.libs.akka.AkkaGuiceSupport;

public class ChatModule extends AbstractModule implements AkkaGuiceSupport {
    @Override
    protected void configure() {
        bindActor(ChatActor.class, "chatActor");
    }
}
