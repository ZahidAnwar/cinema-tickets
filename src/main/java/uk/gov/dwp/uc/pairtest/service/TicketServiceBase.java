package uk.gov.dwp.uc.pairtest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.uc.pairtest.constant.TicketPrice;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.gov.dwp.uc.pairtest.constant.TicketPrice.ADULT_TICKET_PRICE;
import static uk.gov.dwp.uc.pairtest.constant.TicketPrice.CHILD_TICKET_PRICE;
import static uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type.*;
import static uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type.ADULT;

public abstract class TicketServiceBase {

    protected int totalAmountToPay = 0;
    protected int totalSeatsToAllocate = 0;
    private static Logger Log = LoggerFactory.getLogger(TicketServiceBase.class);
    private static final int MAXIMUM_TICKET_ALLOWED_TO_PURCHASE = 20;
    protected boolean validatePurchasedTicket(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {

        Map<TicketTypeRequest.Type, Integer> hm = new HashMap();
        int ticketPrices = 0;
        int numberOfAdults = 0;
        int numberOfChildren = 0;

        if (accountId > 0
                && ticketTypeRequests != null) {

            if (ticketTypeRequests.length > MAXIMUM_TICKET_ALLOWED_TO_PURCHASE) {
                Log.error("Only a maximum of 20 tickets that can be purchased at a time.");
                return false;
            }

            for (TicketTypeRequest request : ticketTypeRequests) {

                if (!hm.containsKey(request.getTicketType())) {
                    hm.put(request.getTicketType(), request.getTicketPrice());
                    ticketPrices += request.getTicketPrice();
                    if(request.getTicketType().equals(ADULT)) {
                        numberOfAdults++;
                    }

                    if(request.getTicketType().equals(CHILD)) {
                        numberOfChildren++;
                    }

                }

                if (!validateTicketPrice(request.getTicketType(), request.getTicketPrice(), request.getNoOfTickets())) {

                    Log.error("Invalid ticket price.");
                    return false;
                }

                totalAmountToPay += request.getTicketPrice();
                totalSeatsToAllocate += request.getNoOfTickets();
            }

            if((!isTicketTypeAdultOnly(hm) && !isChildWithParent(hm))
                    || !validateTicketPrices(ticketPrices, numberOfAdults, numberOfChildren)) {
                Log.error("Child and Infant tickets cannot be purchased without purchasing an Adult ticket.");
                resetValues();
                return false;
            }

            Log.debug("All validation passed.");
            return true;

        }

        return false;
    }

    protected boolean isChildWithParent(Map<TicketTypeRequest.Type, Integer> hm) {

        if (hm.containsKey(CHILD) || hm.containsKey(INFANT)) {
            if(hm.containsKey(ADULT)) {
                return true;
            }
        }

        return false;
    }

    protected boolean isTicketTypeAdultOnly(Map<TicketTypeRequest.Type, Integer> hm) {

        if (hm.containsKey(ADULT)
                && hm.size() == 1
                && hm.get(ADULT) == ADULT_TICKET_PRICE.getPrice()) {
            return true;
        }

        return false;
    }


    protected boolean validateTicketPrice(TicketTypeRequest.Type type, int price, int numberOfTickets) {

        // Assumed customer purchase ticket with bank card only, therefore no change is returned to customer

        if(price > 0 && numberOfTickets > 0) {

            if (type.equals(CHILD)) {

                if (price % CHILD_TICKET_PRICE.getPrice() == 0
                        && price / CHILD_TICKET_PRICE.getPrice() == numberOfTickets) {
                    return true;
                }
            }

            if (type.equals(ADULT)) {

                if (price % ADULT_TICKET_PRICE.getPrice() == 0
                        && price / ADULT_TICKET_PRICE.getPrice() == numberOfTickets) {
                    return true;
                }
            }

        } else {

            if (type.equals(INFANT)) {

                if (price == 0 && numberOfTickets == 0) {
                    return true;
                }
            }
        }

        return false;
    }


    private void resetValues() {
        totalAmountToPay = 0;
        totalSeatsToAllocate = 0;
    }

    protected boolean validateTicketPrices(int totalPrice, int numOfAdult, int numOfChildren) {

        int totalGroup = numOfAdult + numOfChildren;
        if(numOfAdult > 0
                && totalGroup <= MAXIMUM_TICKET_ALLOWED_TO_PURCHASE && (totalPrice - (ADULT_TICKET_PRICE.getPrice() * numOfAdult)
                == CHILD_TICKET_PRICE.getPrice() * numOfChildren)) {
            return true;
        }

        return false;
    }

}
