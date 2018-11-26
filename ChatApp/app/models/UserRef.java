package models;

import akka.actor.ActorRef;

import java.io.Serializable;

public class UserRef implements Serializable {
    private final ActorRef out;
    private final String server;
    private long localId;
    private long id;
    private String name;
    private ActorRef in;

    public UserRef(ActorRef out, long localId, String server) {
        this.out = out;
        this.server = server;
        this.localId = localId;
    }

    public ActorRef getIn() {
        return in;
    }

    public String getServer() {
        return server;
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

    public long getLocalId() {
        return localId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}