package models;

import akka.actor.ActorRef;

public class User {
    private final ActorRef out;
    private String name;
    private ActorRef in;

    public User(ActorRef out) {
        this.out = out;
    }

    public ActorRef getIn() {
        return in;
    }

    public void setIn(ActorRef in) {
        this.in = in;
    }

    public String getName() {
        return name;
    }

    public ActorRef getOut() {
        return out;
    }
}
