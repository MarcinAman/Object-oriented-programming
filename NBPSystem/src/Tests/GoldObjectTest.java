package Tests;

import SourceCode.GoldObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class GoldObjectTest {

    @Test
    public void equals() {
        GoldObject a = new GoldObject("date1",100.0);
        GoldObject b = new GoldObject("date1",100.0);
        GoldObject c = new GoldObject("date2",100.01);
        GoldObject d = new GoldObject("date2",100.0);

        assertEquals(true,a.equals(b));
        assertEquals(false,b.equals(c));
        assertEquals(false,b.equals(d));
        assertEquals(false,c.equals(d));
    }
}