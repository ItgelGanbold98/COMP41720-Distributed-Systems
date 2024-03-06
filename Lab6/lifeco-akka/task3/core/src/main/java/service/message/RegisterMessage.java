package service.message;
import akka.actor.ActorRef;

public class RegisterMessage {
    //Passing the actor ref makes it easy to register the actor to Broker
    public final ActorRef actorRef;
    public final String actorType;

    public RegisterMessage(ActorRef actorRef, String actorType) {
        this.actorRef = actorRef;
        this.actorType = actorType;
    }
}
