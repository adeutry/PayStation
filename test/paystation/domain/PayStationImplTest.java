/**
 * Testcases for the Pay Station system.
 *
 * This source code is from the book "Flexible, Reliable Software: Using
 * Patterns and Agile Development" published 2010 by CRC Press. Author: Henrik B
 * Christensen Computer Science Department Aarhus University
 *
 * This source code is provided WITHOUT ANY WARRANTY either expressed or
 * implied. You may study, use, modify, and distribute it for non-commercial
 * purposes. For any commercial use, see http://www.baerbak.com/
 */
package paystation.domain;

import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class PayStationImplTest {

    PayStation ps;

    @Before
    public void setup() {
        ps = new PayStationImpl();
    }

    /**
     * Entering 5 cents should make the display report 2 minutes parking time.
     */
    @Test
    public void shouldDisplay2MinFor5Cents()
            throws IllegalCoinException {
        ps.addPayment(5);
        assertEquals("Should display 2 min for 5 cents",
                2, ps.readDisplay());
    }

    /**
     * Entering 25 cents should make the display report 10 minutes parking time.
     */
    @Test
    public void shouldDisplay10MinFor25Cents() throws IllegalCoinException {
        ps.addPayment(25);
        assertEquals("Should display 10 min for 25 cents",
                10, ps.readDisplay());
    }

    /**
     * Verify that illegal coin values are rejected.
     */
    @Test(expected = IllegalCoinException.class)
    public void shouldRejectIllegalCoin() throws IllegalCoinException {
        ps.addPayment(17);
    }

    /**
     * Entering 10 and 25 cents should be valid and return 14 minutes parking
     */
    @Test
    public void shouldDisplay14MinFor10And25Cents()
            throws IllegalCoinException {
        ps.addPayment(10);
        ps.addPayment(25);
        assertEquals("Should display 14 min for 10+25 cents",
                14, ps.readDisplay());
    }

    /**
     * Buy should return a valid receipt of the proper amount of parking time
     */
    @Test
    public void shouldReturnCorrectReceiptWhenBuy()
            throws IllegalCoinException {
        ps.addPayment(5);
        ps.addPayment(10);
        ps.addPayment(25);
        Receipt receipt;
        receipt = ps.buy();
        assertNotNull("Receipt reference cannot be null",
                receipt);
        assertEquals("Receipt value must be 16 min.",
                16, receipt.value());
    }

    /**
     * Buy for 100 cents and verify the receipt
     */
    @Test
    public void shouldReturnReceiptWhenBuy100c()
            throws IllegalCoinException {
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(25);
        ps.addPayment(25);

        Receipt receipt;
        receipt = ps.buy();
        assertEquals(40, receipt.value());
    }

    /**
     * Verify that the pay station is cleared after a buy scenario
     */
    @Test
    public void shouldClearAfterBuy()
            throws IllegalCoinException {
        ps.addPayment(25);
        ps.buy(); // I do not care about the result
        // verify that the display reads 0
        assertEquals("Display should have been cleared",
                0, ps.readDisplay());
        // verify that a following buy scenario behaves properly
        ps.addPayment(10);
        ps.addPayment(25);
        assertEquals("Next add payment should display correct time",
                14, ps.readDisplay());
        Receipt r = ps.buy();
        assertEquals("Next buy should return valid receipt",
                14, r.value());
        assertEquals("Again, display should be cleared",
                0, ps.readDisplay());
    }

    /**
     * Verify that cancel clears the pay station
     */
    @Test
    public void shouldClearAfterCancel()
            throws IllegalCoinException {
        ps.addPayment(10);
        ps.cancel();
        assertEquals("Cancel should clear display",
                0, ps.readDisplay());
        ps.addPayment(25);
        assertEquals("Insert after cancel should work",
                10, ps.readDisplay());
    }
    
    /**
     * Calling empty should return the total amount of money entered
     */
    @Test
    public void shouldReturnAmountEntered()
            throws IllegalCoinException
    {
        ps.addPayment(10);
        ps.addPayment(5);
        assertEquals("Calling empty should return total money",
                15, ps.empty());
    }
    
    /**
     * Canceling a coin entry should cause the empty method to return zero.
     */
    @Test
    public void shouldNotAddToEmpty()
            throws IllegalCoinException
    {
        ps.addPayment(5);
        ps.addPayment(5);
        ps.addPayment(5);
        ps.cancel();
        assertEquals("Canceled entry does not add to amount returned by empty",
                0,ps.empty());
    }
    
     /**
     * Calling empty resets the total amount entered to zero
     */
    @Test
    public void shouldResetToZero()
            throws IllegalCoinException
    {
        ps.addPayment(10);
        ps.addPayment(5);
        ps.empty();
        assertEquals("Call to empty should reset time total to zero",
                0, ps.readDisplay() );
        
    } 
    
    /**
     * Calling cancel after inserting one coin returns a map containing one coin
     * entered.
     */
    @Test
    public void shouldReturnOneCoin() throws IllegalCoinException
    {
        ps.addPayment(10);
        Map<Integer,Integer> map = ps.cancel();
        assertEquals("Call to cancel should return single coin",
                1, map.size());
        assertEquals("Should return single 10 cent coin",
                (Integer)1, map.get(10));
        
    }
    
     /**
     * Calling cancel after returning several coins returns a map of a mixture
     * of coins.
     */
    @Test
    public void shouldReturnCoinMixture() throws IllegalCoinException
    {
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(10);
        Map<Integer,Integer> map = ps.cancel();
        assertEquals("Call to cancel should return two coins", 
                2, map.size());
        assertEquals("Call to cancel should return quarter",  
                true, map.containsKey(25));
        assertEquals("Call to cancel should return quarter and nickel", 
                true, map.containsKey(5));
        assertEquals("Call to cancel should return one nickel", 
                (Integer)1, map.get(5));
        assertEquals("Call to cancel should return one quarter", 
                (Integer)1, map.get(25));
        
    }
    
    
    /**
     * A call to cancel should not return a map which contains a key for a coin
     * not entered.
     */
    @Test
    public void shouldNotReturnCoinNotEntered() throws IllegalCoinException
    {
        ps.addPayment(5);
        ps.addPayment(5);
        ps.addPayment(5);
        Map<Integer,Integer> map = ps.cancel();
        assertEquals("cancelling transaction should not return coins which were not entered",
                false, map.containsKey(25) );
    }
}
 