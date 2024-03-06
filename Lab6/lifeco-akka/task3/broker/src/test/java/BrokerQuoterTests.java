import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import akka.actor.Props;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import service.core.ClientInfo;
import service.core.Quotation;
import service.message.ClientMessage;
import service.message.OfferMessage;
import service.broker.BrokerQuoter;
import service.message.RegisterMessage;
import service.quoter.AFQuoter;
import service.quoter.DGQuoter;
import service.quoter.GAQuoter;

import java.time.Duration;

public class BrokerQuoterTests {
    static ActorSystem system;
    @BeforeClass
    public static void setup() {system = ActorSystem.create(); }
    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }
    @Test
    public void quoterTest() {
        //Send ClientMessage to BrokerQuoter and wait for OfferMessage to be sent back in 2 seconds
        final Props props = Props.create(BrokerQuoter.class);
        final ActorRef subject = system.actorOf(props);

        //Register AF to Broker
        final Props prop_af = Props.create(AFQuoter.class);
        final ActorRef subject_af = system.actorOf(prop_af);
        subject.tell(new RegisterMessage(subject_af, "QService"), subject);
        //Register DG to Broker
        final Props prop_dg = Props.create(DGQuoter.class);
        final ActorRef subject_dg = system.actorOf(prop_dg);
        subject.tell(new RegisterMessage(subject_dg, "QService"), subject);
        //Register GA to Broker
        final Props prop_ga = Props.create(GAQuoter.class);
        final ActorRef subject_ga = system.actorOf(prop_ga);
        subject.tell(new RegisterMessage(subject_ga, "QService"), subject);


        final TestKit probe = new TestKit(system);
        subject.tell(
                new ClientMessage(1l,
                        new ClientInfo("Niki Collier", ClientInfo.FEMALE, 49, 1.5494, 80, false, false)),
                probe.getRef());
        OfferMessage response = probe.expectMsgClass(Duration.ofSeconds(4), OfferMessage.class);
        for (Quotation quotation : response.getQuotations()){
            System.out.println("Response from broker: " + quotation.toString());
        }
    }
}