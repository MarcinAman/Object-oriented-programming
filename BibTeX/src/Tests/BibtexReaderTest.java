package Tests;

import SourceCode.BibtexElement;
import SourceCode.BibtexFileExceptions;
import SourceCode.BibtexReader;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class BibtexReaderTest {
    @Test
    public void generateNewBibtexElementTest() throws BibtexFileExceptions {
        BibtexReader reader = new BibtexReader();
        BibtexElement e1 = new BibtexElement("article-minimal","article",null,null);
        assertEquals(e1,reader.generateNewBibtexElement("@ARTICLE{article-minimal,"));
        BibtexElement e2 = new BibtexElement("book-minimal","book",null,null);
        assertEquals(e2,reader.generateNewBibtexElement("@BOOK{book-minimal,"));
        BibtexElement e3 = new BibtexElement("mastersthesis-full","mastersthesis",null,null);
        assertEquals(e3,reader.generateNewBibtexElement("@MASTERSTHESIS{mastersthesis-full,"));
    }

    @Test
    public void toStringTest(){
        BibtexReader reader = new BibtexReader();
        String key = "all";
        String sign = "";
        assertEquals(reader.toString()+"\n",reader.toString(key,sign,"all"));
    }
}