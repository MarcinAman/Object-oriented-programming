package Tests;

import SourceCode.DrivingClass;
import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.Assert.*;

public class DrivingClassTest {

    @Test
    public void gatherDayCurrencyDataTableA() throws IOException, JSONException {
        DrivingClass dv = new DrivingClass();
        assertEquals("Currency value: funt szterling\tGBP\t5.9928\n",dv.gatherDayCurrencyDataTableA("GBP","2015-12-15"));
        assertEquals("Currency value: frank szwajcarski\tCHF\t3.5672\n",dv.gatherDayCurrencyDataTableA("CHF","2017-12-29"));
        assertEquals("Currency value: frank szwajcarski\tCHF\t4.119\n",dv.gatherDayCurrencyDataTableA("CHF","2016-12-29"));
        assertEquals("Currency value: dolar amerykański\tUSD\t3.83\n", dv.gatherDayCurrencyDataTableA("USD","2016-05-04"));
        assertEquals("Currency value: dolar amerykański\tUSD\t3.989\n",dv.gatherDayCurrencyDataTableA("USD","2017-04-04"));
    }

    @Test
    public void getMinAskCurrency() throws IOException, JSONException {
        DrivingClass drivingClass = new DrivingClass();
        assertEquals("Minimal ask currency object is: forint (Węgry)\tHUF\t0.013683",drivingClass.getMinAskCurrency("2017-11-21"));
    }

    @Test
    public void getMaxDiffrenceCurrencies() {
        DrivingClass drivingClass = new DrivingClass();
    }

    @Test
    public void getMaxMinOfCurrency() throws ParseException, JSONException, IOException {
        DrivingClass drivingClass = new DrivingClass();
        assertEquals("Min value: funt szterling\t2012-01-31\t5.0496\n" +
                "Max value: funt szterling\t2012-01-05\t5.4616",
                drivingClass.getMaxMinOfCurrency("GBP","2012-01-01","2012-01-31"));
        assertEquals("Min value: frank szwajcarski\t2012-01-27\t3.4966\n" +
                        "Max value: frank szwajcarski\t2012-01-05\t3.7037",
                drivingClass.getMaxMinOfCurrency("CHF","2012-01-01","2012-01-31"));
    }

    @Test
    public void parseDateString() throws ParseException {
        DrivingClass drivingClass = new DrivingClass();
        assertEquals("2017-01-01",drivingClass.format.format(drivingClass.parseDateString("2017-01-01").getTime()));
        assertEquals("2015-12-11",drivingClass.format.format(drivingClass.parseDateString("2015-12-11").getTime()));
    }

    @Test
    public void gatherGoldData() throws Exception {
        DrivingClass drivingClass = new DrivingClass();
        assertEquals("Current gold value: 165.83\n",drivingClass.gatherGoldData("2013-01-02"));
        assertEquals("Current gold value: 142.32\n",drivingClass.gatherGoldData("2015-11-02"));
        assertEquals("Current gold value: 152.47\n",drivingClass.gatherGoldData("2017-05-11"));
    }

    @Test
    public void gatherAverageGoldPrice() throws JSONException, ParseException, IOException {
        DrivingClass drivingClass = new DrivingClass();
        assertEquals("Avg gold price in period: 2017-11-15 to 2017-11-20 is 148.11",
                drivingClass.gatherAverageGoldPrice("2017-11-15","2017-11-20")); //148.1075 to be exact
        assertEquals("Avg gold price in period: 2016-11-15 to 2016-11-20 is 161.76",
                drivingClass.gatherAverageGoldPrice("2016-11-15","2016-11-20")); //161.7575
    }
}