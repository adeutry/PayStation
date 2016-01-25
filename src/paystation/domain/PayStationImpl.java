package paystation.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the pay station.
 *
 * Responsibilities:
 *
 * 1) Accept payment; 
 * 2) Calculate parking time based on payment; 
 * 3) Know earning, parking time bought; 
 * 4) Issue receipts; 
 * 5) Handle buy and cancel events.
 *
 * This source code is from the book "Flexible, Reliable Software: Using
 * Patterns and Agile Development" published 2010 by CRC Press. Author: Henrik B
 * Christensen Computer Science Department Aarhus University
 *
 * This source code is provided WITHOUT ANY WARRANTY either expressed or
 * implied. You may study, use, modify, and distribute it for non-commercial
 * purposes. For any commercial use, see http://www.baerbak.com/
 */
public class PayStationImpl implements PayStation {
    
    private int insertedSoFar;
    private int timeBought;

    @Override
    public void addPayment(int coinValue)
            throws IllegalCoinException {
        switch (coinValue) {
            case 5: break;
            case 10: break;
            case 25: break;
            default:
                throw new IllegalCoinException("Invalid coin: " + coinValue);
        }
        insertedSoFar += coinValue;
        timeBought = insertedSoFar / 5 * 2;
    }

    @Override
    public int readDisplay() {
        return timeBought;
    }

    @Override
    public Receipt buy() {
        Receipt r = new ReceiptImpl(timeBought);
        reset();
        return r;
    }

    @Override
    public Map<Integer, Integer> cancel(){
        Map<Integer, Integer> coinMap = new HashMap<Integer, Integer>();
        while(insertedSoFar > 0)
        {
            if(insertedSoFar >= 25)
            {
                if(coinMap.containsKey(25))
                {
                    coinMap.put(25, coinMap.get(25) + 1);
                }else{
                    coinMap.put(25, 1);
                }
                
                insertedSoFar -= 25;
                
            }else if( insertedSoFar >= 10 )
            {
                if(coinMap.containsKey(10))
                {
                    coinMap.put(10, coinMap.get(10) + 1);
                }else{
                    coinMap.put(10, 1);
                }
                
                insertedSoFar -= 10;
                
            }else{
                
                if(coinMap.containsKey(5))
                {
                    coinMap.put(5, coinMap.get(5) + 1);
                }else{
                    coinMap.put(5, 1);
                }
                
                insertedSoFar -= 10;
            }
        }
        reset();
        return coinMap;
    }
    
    @Override
    public int empty()
    {
        int inserted = insertedSoFar;
        reset();
        return inserted;
    }
    
    private void reset() {
        timeBought = insertedSoFar = 0;
    }
}
