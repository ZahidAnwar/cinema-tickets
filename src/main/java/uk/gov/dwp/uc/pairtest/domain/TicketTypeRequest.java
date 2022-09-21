package uk.gov.dwp.uc.pairtest.domain;

/**
 * Immutable Object
 */
public class TicketTypeRequest {

    private int noOfTickets;
    private Type type;

    private int ticketPrice;

    public TicketTypeRequest(Type type, int noOfTickets, int ticketPrice) {
        this.type = type;
        this.noOfTickets = noOfTickets;
        this.ticketPrice = ticketPrice;
    }

    public int getNoOfTickets() {
        return noOfTickets;
    }

    public Type getTicketType() {
        return type;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public enum Type {
        ADULT, CHILD , INFANT
    }

}
