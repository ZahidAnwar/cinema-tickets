package uk.gov.dwp.uc.pairtest.service;

import org.junit.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TicketServiceTest {


    @Test(expected = Exception.class)
    public void testPurchaseTicketsException() {
        //TicketServiceImpl ticketService = mock(TicketServiceImpl.class);
        //doThrow().when(ticketService).purchaseTickets(any(), isNull());

        TicketServiceImpl ticketService = new TicketServiceImpl();
        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1, 20);
        ticketService.purchaseTickets(0L, ticketTypeRequest);

        assertEquals(0, ticketService.totalAmountToPay);
        assertEquals(0, ticketService.totalSeatsToAllocate);

        ticketService.purchaseTickets(2L, new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1, 0),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1, 10),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1, 0));

        assertEquals(0, ticketService.totalAmountToPay);
        assertEquals(0, ticketService.totalSeatsToAllocate);

        ticketService.purchaseTickets(1L);

        assertEquals(0, ticketService.totalAmountToPay);
        assertEquals(0, ticketService.totalSeatsToAllocate);

    }

    @Test
    public void testPurchaseTickets() {
        TicketServiceImpl ticketService = new TicketServiceImpl();
        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1, 20);

        ticketService.purchaseTickets(1L,
                ticketTypeRequest, ticketTypeRequest, ticketTypeRequest, ticketTypeRequest, ticketTypeRequest,
                ticketTypeRequest, ticketTypeRequest, ticketTypeRequest, ticketTypeRequest, ticketTypeRequest,
                ticketTypeRequest, ticketTypeRequest, ticketTypeRequest, ticketTypeRequest, ticketTypeRequest,
                ticketTypeRequest, ticketTypeRequest, ticketTypeRequest, ticketTypeRequest, ticketTypeRequest);

        assertEquals(400, ticketService.totalAmountToPay);
        assertEquals(20, ticketService.totalSeatsToAllocate);

        ticketService = new TicketServiceImpl();

        ticketService.purchaseTickets(1L, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1, 20),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1, 10),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 0, 0));

        assertEquals(30, ticketService.totalAmountToPay);
        assertEquals(2, ticketService.totalSeatsToAllocate);
    }

    @Test
    public void testValidatePurchasedTicket() {
        TicketServiceImpl ticketService = new TicketServiceImpl();
        assertFalse(ticketService.validatePurchasedTicket(0L));
        assertFalse(ticketService.validatePurchasedTicket(1L, null));

        TicketTypeRequest ticketTypeRequest;
        for(int i =0; i<10; i++) {
            ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1, i);
            assertFalse(ticketService.validatePurchasedTicket(1L, ticketTypeRequest));
        }

        ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1, 11);
        assertFalse(ticketService.validatePurchasedTicket(1L, ticketTypeRequest));


        TicketTypeRequest ticketTypeRequest1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1, 20);

        assertFalse(ticketService.validatePurchasedTicket(1L,
                ticketTypeRequest1, ticketTypeRequest1, ticketTypeRequest1, ticketTypeRequest1, ticketTypeRequest1, ticketTypeRequest1,
                ticketTypeRequest1, ticketTypeRequest1, ticketTypeRequest1, ticketTypeRequest1, ticketTypeRequest1, ticketTypeRequest1,
                ticketTypeRequest1, ticketTypeRequest1, ticketTypeRequest1, ticketTypeRequest1, ticketTypeRequest1, ticketTypeRequest1,
                ticketTypeRequest1, ticketTypeRequest1, ticketTypeRequest1));

        //20 adult tickets
        assertTrue(ticketService.validatePurchasedTicket(1L,
                ticketTypeRequest1, ticketTypeRequest1, ticketTypeRequest1, ticketTypeRequest1, ticketTypeRequest1, ticketTypeRequest1,
                ticketTypeRequest1, ticketTypeRequest1, ticketTypeRequest1, ticketTypeRequest1, ticketTypeRequest1, ticketTypeRequest1,
                ticketTypeRequest1, ticketTypeRequest1, ticketTypeRequest1, ticketTypeRequest1, ticketTypeRequest1, ticketTypeRequest1,
                ticketTypeRequest1, ticketTypeRequest1));
    }

    @Test
    public void testIsChildWithParent() {
        TicketServiceImpl ticketService = new TicketServiceImpl();

        Map<TicketTypeRequest.Type, Integer> hm = new HashMap();
        hm.put(TicketTypeRequest.Type.CHILD, 10);
        assertFalse(ticketService.isChildWithParent(hm));

        hm = new HashMap();
        hm.put(TicketTypeRequest.Type.INFANT, 0);
        assertFalse(ticketService.isChildWithParent(hm));

        hm = new HashMap();
        hm.put(TicketTypeRequest.Type.INFANT, 0);
        hm.put(TicketTypeRequest.Type.CHILD, 10);
        assertFalse(ticketService.isChildWithParent(hm));

        hm = new HashMap();
        hm.put(TicketTypeRequest.Type.INFANT, 0);
        hm.put(TicketTypeRequest.Type.CHILD, 10);
        hm.put(TicketTypeRequest.Type.ADULT, 20);
        assertTrue(ticketService.isChildWithParent(hm));
    }

    @Test
    public void testIsTicketTypeAdultOnly() {
        TicketServiceImpl ticketService = new TicketServiceImpl();

        Map<TicketTypeRequest.Type, Integer> hm = new HashMap();
        hm.put(TicketTypeRequest.Type.CHILD, 10);
        assertFalse(ticketService.isTicketTypeAdultOnly(hm));

        hm = new HashMap();
        hm.put(TicketTypeRequest.Type.INFANT, 0);
        assertFalse(ticketService.isTicketTypeAdultOnly(hm));

        hm = new HashMap();
        hm.put(TicketTypeRequest.Type.INFANT, 0);
        hm.put(TicketTypeRequest.Type.CHILD, 10);
        assertFalse(ticketService.isTicketTypeAdultOnly(hm));

        hm = new HashMap();
        hm.put(TicketTypeRequest.Type.ADULT, 20);
        assertTrue(ticketService.isTicketTypeAdultOnly(hm));

        for(int i = 1; i <20; i++) {
            hm = new HashMap();
            hm.put(TicketTypeRequest.Type.ADULT, i);
            assertFalse(ticketService.isTicketTypeAdultOnly(hm));
        }

    }

    @Test
    public void testValidTicketPrice() {
        TicketServiceImpl ticketService = new TicketServiceImpl();

        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.CHILD, 0, 0));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.CHILD, 35, 3));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.CHILD, 40, 6));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.CHILD, 10, 2));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.CHILD, 197, 19));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.CHILD, 200, 23));
        assertTrue(ticketService.validateTicketPrice(TicketTypeRequest.Type.CHILD, 190, 19));
        assertTrue(ticketService.validateTicketPrice(TicketTypeRequest.Type.CHILD, 150, 15));
        assertTrue(ticketService.validateTicketPrice(TicketTypeRequest.Type.CHILD, 170, 17));

        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.ADULT, 0, 0));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.ADULT, 65, 3));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.ADULT, 110, 6));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.ADULT, 50, 2));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.ADULT, 200, 11));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.ADULT, 500, 23));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.ADULT, 99, 5));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.ADULT, 77, 7));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.ADULT, 90, 4));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.ADULT, 300, 11));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.ADULT, 500, 16));
        assertTrue(ticketService.validateTicketPrice(TicketTypeRequest.Type.ADULT, 380, 19));
        assertTrue(ticketService.validateTicketPrice(TicketTypeRequest.Type.ADULT, 360, 18));
        assertTrue(ticketService.validateTicketPrice(TicketTypeRequest.Type.ADULT, 320, 16));
        assertTrue(ticketService.validateTicketPrice(TicketTypeRequest.Type.ADULT, 300, 15));

        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.INFANT, 30, 3));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.INFANT, 60, 6));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.INFANT, 20, 2));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.INFANT, 190, 19));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.INFANT, 230, 23));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.INFANT, 60, 3));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.INFANT, 120, 6));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.INFANT, 40, 2));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.INFANT, 360, 19));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.INFANT, 460, 23));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.INFANT, 0, 1));
        assertFalse(ticketService.validateTicketPrice(TicketTypeRequest.Type.INFANT, 1, 0));
        assertTrue(ticketService.validateTicketPrice(TicketTypeRequest.Type.INFANT, 0, 0));
    }

    @Test
    public void testVValidateTicketPrices() {
        TicketServiceImpl ticketService = new TicketServiceImpl();
        assertTrue(ticketService.validateTicketPrices(70, 2, 3));
        assertTrue(ticketService.validateTicketPrices(70, 2, 3));
        assertTrue(ticketService.validateTicketPrices(310, 11, 9));
        assertTrue(ticketService.validateTicketPrices(220, 2, 18));
        assertTrue(ticketService.validateTicketPrices(380, 18, 2));
        assertTrue(ticketService.validateTicketPrices(390, 19, 1));
        assertTrue(ticketService.validateTicketPrices(210, 1, 19));
        assertFalse(ticketService.validateTicketPrices(100, 0, 10));
        assertFalse(ticketService.validateTicketPrices(100, 0, 20));
        assertFalse(ticketService.validateTicketPrices(200, 1, 19));
    }
}
