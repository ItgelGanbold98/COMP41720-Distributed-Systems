package service.quoter;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import service.dodgygeezers.DGQService;
import service.core.Quotation;
import service.message.ClientMessage;
import service.message.QuotationMessage;

public class DGQuoter extends AbstractActor {
    private DGQService service = new DGQService();
    @Override
    public Receive createReceive() {
        return new ReceiveBuilder()
                .match(ClientMessage.class,
                        msg -> {
                            System.out.println("**DODGYGEEZERS ACTOR STARTED**");
                            Quotation quotation = service.generateQuotation(msg.getInfo());
                            getSender().tell(new QuotationMessage(msg.getToken(), quotation), getSelf());
                            System.out.println("Dodgygeezers sending message to: " + getSender());
                        })
                .build();
    }
}