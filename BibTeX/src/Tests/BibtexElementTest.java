package Tests;

import SourceCode.BibtexElement;
import SourceCode.BibtexFileExceptions;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BibtexElementTest {
    @Test
    public void equalsTest() throws BibtexFileExceptions {
        BibtexElement e1 = new BibtexElement("Key","Kind",null,null);
        BibtexElement e2 = new BibtexElement("Key","Kind",null,null);
        BibtexElement e3 = new BibtexElement("Key2","Kind",null,null);
        assertTrue(e1.equals(e2));
        assertTrue(!e1.equals(e3));
        assertTrue(e1.equals(e1));
        BibtexElement e4 = new BibtexElement("Key","Kind",null,null);
        e4.addInformationToData("category","value");
        assertTrue(!e1.equals(e4));
    }

}