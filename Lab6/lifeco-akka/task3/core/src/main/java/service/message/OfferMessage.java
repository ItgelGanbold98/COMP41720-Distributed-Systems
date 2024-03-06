package service.message;

import service.core.ClientInfo;
import service.core.Quotation;

import java.util.LinkedList;

public class OfferMessage implements java.io.Serializable {
    private ClientInfo info;
    private LinkedList<Quotation> quotations;
    public OfferMessage(ClientInfo info, LinkedList<Quotation> quotations) {
        this.info = info;
        this.quotations = quotations;
    }
    public void setQuotations(LinkedList<Quotation> receivedQuotations) {this.quotations = receivedQuotations;}
    public ClientInfo getInfo() {
        return info;
    }
    public LinkedList<Quotation> getQuotations() {
        return quotations;
    }
}