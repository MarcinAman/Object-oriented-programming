package Tests;

import SourceCode.CurrencyObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class CurrencyObjectTest {

    @Test
    public void calculateDiffrenceInPrice() {
        CurrencyObject object = new CurrencyObject(null, null, 100.0,200.0);
        Double valueDiff = object.calculateDiffrenceInPrice();
        assertEquals((Double)100.0,valueDiff);
    }

    @Test
    public void calculateDiffrenceInAskValue() {
        CurrencyObject object = new CurrencyObject(null, null, 100.0,200.0);
        CurrencyObject object2 = new CurrencyObject(null, null, 200.0,100.0);
        assertEquals((Double)100.0,object.calculateDiffrenceInAskValue(object2));
        assertEquals((Double)100.0,object2.calculateDiffrenceInAskValue(object));
        assertEquals((Double)0.0,object.calculateDiffrenceInAskValue(object));
    }

}