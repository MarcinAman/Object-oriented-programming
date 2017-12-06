package Tests;

import SourceCode.BibtexElement;
import SourceCode.BibtexFileExceptions;
import SourceCode.BibtexValidator;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class BibtexValidatorTest {
    @Test
    public void removeBracketsTest() throws Exception {
        BibtexValidator val = new BibtexValidator(null,null);
        assertEquals("S",val.removeBrackets("{S}"));
        assertEquals("Some Value",val.validateString(null,"{Some Value}"));
        assertEquals("s{S}s",val.removeBrackets("s{S}s"));
        assertEquals("The Gnats and Gnus Document Preparation System",val.removeBrackets("{The Gnats and Gnus Document Preparation System}"));
    }

    @Test(expected = BibtexFileExceptions.class)
    public void removeBracketsTestException() throws  Exception{
        BibtexValidator val = new BibtexValidator(null,null);
        val.removeBrackets("{ssssss");
        val.removeBrackets("sss}ss");
    }

    @Test
    public void removeQuotesTest() throws Exception {
        BibtexValidator val = new BibtexValidator(null,null);
        assertEquals("s",val.removeQuotes("\"s\""));
        assertEquals("name",val.removeQuotes(" \" name\""));
        assertEquals("{Some Value}",val.validateString("","\"{Some Value}\""));
    }
    @Test(expected = BibtexFileExceptions.class)
    public void removeQuotesTestException() throws  Exception{
        BibtexValidator val = new BibtexValidator(null,null);
        val.removeQuotes("\"ssssss");
    }

    @Test
    public void validateStringTest() throws Exception {
        Map<String,String> validatorMap = new HashMap<>();
        validatorMap.put("Some","More");
        BibtexValidator val = new BibtexValidator(validatorMap,null);
        assertEquals("Some Value",val.validateString("","{Some Value}"));
        assertEquals("More {Some} Value",val.validateString("","\"Some {Some} Value\""));
        assertEquals("More {Some} More Value",val.validateString("","Some {Some} Some Value"));
    }

    @Test(expected = BibtexFileExceptions.class)
    public void checkReqiedFieldsWithException() throws Exception{
        Map<String, String[]> requiedMap = new HashMap<>();
        requiedMap.put("FieldName",new String[]{"Field1","Field3"});
        BibtexElement element = new BibtexElement("Key","FieldName",null,requiedMap);
        element.addInformationToData("Field1","Field1Value");
        element.addInformationToData("Field2","Field2Value"); //Lack of third value
        element.validateLast();
    }

    @Test
    public void checkReqiedFieldsWithoutException() throws Exception{
        Map<String, String[]> requiedMap = new HashMap<>();
        requiedMap.put("FieldName",new String[]{"Field1","Field2"});
        BibtexElement element = new BibtexElement("Key","FieldName",null,requiedMap);
        element.addInformationToData("Field1","Field1Value");
        element.addInformationToData("Field2","Field2Value");
        element.addInformationToData("Field3","Field2Value");
        element.validateLast();
    }

    @Test
    public void checkNameFieldTest(){
        BibtexValidator bibtexElement = new BibtexValidator(null,null);
        assertEquals("Donald Trump",bibtexElement.checkNameField("Trump, Donald"));
        assertEquals("{Trump, Donald}",bibtexElement.checkNameField("{Trump, Donald}"));
        assertEquals("{ Donald, Trump}",bibtexElement.checkNameField("{ Donald, Trump}"));
        assertEquals("{Donald} Trump",bibtexElement.checkNameField("Trump, {Donald}"));
    }

}