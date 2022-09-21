package uk.gov.dwp.uc.pairtest.constant;

/*
 * For all price constant
 *
 * Can be extended in future for new price
 */
public enum TicketPrice {
    CHILD_TICKET_PRICE(10), ADULT_TICKET_PRICE(20);

    private int price;

    TicketPrice(int p) {
        this.price = p;
    }

    public int getPrice() {
        return price;
    }


}
