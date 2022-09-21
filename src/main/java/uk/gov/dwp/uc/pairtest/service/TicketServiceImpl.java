package uk.gov.dwp.uc.pairtest.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;


public class TicketServiceImpl extends TicketServiceBase implements TicketService {

    /**
     * Should only have private methods other than the one below.
     */

    private final TicketPaymentService ticketPaymentService = new TicketPaymentServiceImpl();
    private final SeatReservationService seatReservationService = new SeatReservationServiceImpl();
    private static Logger Log = LoggerFactory.getLogger(TicketServiceImpl.class);

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {

        if (validatePurchasedTicket(accountId, ticketTypeRequests)) {
            Log.debug("Successfully validated purchase ticket. Let's use payment service.");

            ticketPaymentService.makePayment(accountId, totalAmountToPay);
            seatReservationService.reserveSeat(accountId, totalSeatsToAllocate);

        } else {
            Log.error("Invalid ticket purchase request.");
            throw new InvalidPurchaseException();
        }
    }



}
