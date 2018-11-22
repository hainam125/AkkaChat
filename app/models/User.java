package models;

import akka.actor.ActorRef;

public class User {
    private final ActorRef out;
    private final String name;

    public User(String name, ActorRef out) {
        this.name = name;
        this.out = out;
    }

    public String getName() {
        return name;
    }

    public ActorRef getOut() {
        return out;
    }
}
