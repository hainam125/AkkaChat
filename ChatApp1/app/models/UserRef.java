package models;

import akka.actor.ActorRef;

public class UserRef {
    private final ActorRef out;
    private User user;
    private ActorRef in;

    public UserRef(ActorRef out) {
        this.out = out;
    }

    public ActorRef getIn() {
        return in;
    }

    public void setIn(ActorRef in) {
        this.in = in;
    }

    public ActorRef getOut() {
        return out;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }
}
