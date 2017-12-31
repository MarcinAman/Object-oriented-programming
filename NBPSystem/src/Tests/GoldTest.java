package Tests;

import SourceCode.DrivingClass;
import SourceCode.Gold;
import SourceCode.GoldObject;
import org.json.JSONException;

import java.io.IOException;

import static org.junit.Assert.*;

public class GoldTest {

    @org.junit.Test
    public void loadGoldRates() throws IOException, JSONException {
        DrivingClass dr = new DrivingClass();
        String apiResponse = dr.getAllData("http://api.nbp.pl/api/cenyzlota/2017-01-02/?format=json");
        Gold gold = new Gold("2017-01-02");
        gold.loadGoldRates(apiResponse);

        Gold otherGold = new Gold("2017-01-02");
        otherGold.prices.add(new GoldObject("2017-01-02",155.76));


        assertEquals(true,gold.equals(otherGold));
        otherGold.prices.add(new GoldObject("2017-01-02",155.76));
        assertEquals(false,gold.equals(otherGold));
    }

    @org.junit.Test
    public void getDay() {
        Gold gold = new Gold("2017-01-02");
        Gold gold2 = new Gold("2017-01-02");

        assertEquals("2017-01-02",gold.getDay());
        assertEquals("2017-01-02",gold2.getDay());
        assertEquals(true,gold.getDay().equals(gold2.getDay()));
    }
}