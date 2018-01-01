package Tests;

import SourceCode.AllCurrencyTypeA;
import SourceCode.CurrencyObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class AllCurrencyTypeATest {

    //No point in testing getAllCurrencyMethod as it is basically a use of java-json package

    @Test
    public void getFirstValue() {
        AllCurrencyTypeA currencyTypeA = new AllCurrencyTypeA("2017-12-31");
        CurrencyObject added = new CurrencyObject(null, "some", 100.01, (String)null);
        currencyTypeA.currencyData.add(added);
        assertEquals(added.getValueAsk(),currencyTypeA.getFirstValue());
    }

    @Test
    public void getMaxCurrencyObject() {
        AllCurrencyTypeA currencyTypeA = new AllCurrencyTypeA("2017-12-31");
        CurrencyObject a1 = new CurrencyObject(null, "some1", 100.01, (String)null);
        CurrencyObject a2 = new CurrencyObject(null, "some2", 99.0, (String)null);
        CurrencyObject a3 = new CurrencyObject(null, "some3", 101.01, (String)null);
        CurrencyObject a4 = new CurrencyObject(null, "some4", 99.101, (String)null);
        currencyTypeA.currencyData.add(a1);
        currencyTypeA.currencyData.add(a2);
        currencyTypeA.currencyData.add(a3);
        currencyTypeA.currencyData.add(a4);
        assertEquals(a3,currencyTypeA.getMaxCurrencyObject());
    }

    @Test
    public void getMinCurrencyObject() {
        AllCurrencyTypeA currencyTypeA = new AllCurrencyTypeA("2017-12-31");
        CurrencyObject a1 = new CurrencyObject(null, "some1", 100.01, (String)null);
        CurrencyObject a2 = new CurrencyObject(null, "some2", 99.0, (String)null);
        CurrencyObject a3 = new CurrencyObject(null, "some3", 101.01, (String)null);
        CurrencyObject a4 = new CurrencyObject(null, "some4", 99.101, (String)null);
        currencyTypeA.currencyData.add(a1);
        currencyTypeA.currencyData.add(a2);
        currencyTypeA.currencyData.add(a3);
        currencyTypeA.currencyData.add(a4);
        assertEquals(a2,currencyTypeA.getMinCurrencyObject());
    }

    @Test
    public void getCurrencyAskValue() {
        AllCurrencyTypeA currencyTypeA = new AllCurrencyTypeA("2017-12-31");
        CurrencyObject a1 = new CurrencyObject(null, "some1", 100.01, (String)null);
        CurrencyObject a2 = new CurrencyObject(null, "some2", 99.0, (String)null);
        CurrencyObject a3 = new CurrencyObject(null, "some3", 101.01, (String)null);
        CurrencyObject a4 = new CurrencyObject(null, "some4", 99.101, (String)null);
        currencyTypeA.currencyData.add(a1);
        currencyTypeA.currencyData.add(a2);
        currencyTypeA.currencyData.add(a3);
        currencyTypeA.currencyData.add(a4);
        assertEquals((Double)100.01,currencyTypeA.getCurrencyAskValue("some1"));
        assertEquals((Double)99.0,currencyTypeA.getCurrencyAskValue("some2"));
        assertEquals((Double)101.01,currencyTypeA.getCurrencyAskValue("some3"));
        assertEquals((Double)99.101,currencyTypeA.getCurrencyAskValue("some4"));

    }
}