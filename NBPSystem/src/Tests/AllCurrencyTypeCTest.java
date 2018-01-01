package Tests;

import SourceCode.AllCurrencyTypeC;
import SourceCode.CurrencyObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class AllCurrencyTypeCTest {

    //No point in testing getAllCurrencyMethod as it is basically a use of java-json package

    public void prepareData(AllCurrencyTypeC currencyTypeC){
        CurrencyObject a1 = new CurrencyObject(null, "some1", 100.01, 100.2);
        CurrencyObject a2 = new CurrencyObject(null, "some2", 99.0, 101.0);
        CurrencyObject a3 = new CurrencyObject(null, "some3", 101.01, 101.0);
        CurrencyObject a4 = new CurrencyObject(null, "some4", 99.101, 99.1);
        currencyTypeC.currencyData.add(a1);
        currencyTypeC.currencyData.add(a2);
        currencyTypeC.currencyData.add(a3);
        currencyTypeC.currencyData.add(a4);
    }

    @Test
    public void getMinCurrencyObject() {
        AllCurrencyTypeC currencyTypeC = new AllCurrencyTypeC("2017-01-01");
        prepareData(currencyTypeC);
        CurrencyObject a5 = new CurrencyObject(null,"some5",98.99,90.0);
        currencyTypeC.currencyData.add(a5);
        assertEquals(a5,currencyTypeC.getMinCurrencyObject());
    }

    @Test
    public void getMaxDiffrenceCurrency() {
        AllCurrencyTypeC currencyTypeC = new AllCurrencyTypeC("2017-01-01");
        prepareData(currencyTypeC);
        CurrencyObject a5 = new CurrencyObject(null,"some5",98.99,103.0);
        currencyTypeC.currencyData.add(a5);
        int size = currencyTypeC.currencyData.size();
        assertEquals(a5,currencyTypeC.getMaxDiffrenceCurrency());
        assertEquals(size-1,currencyTypeC.currencyData.size());
    }

}