package SourceCode;

public class BibtexFileExceptions extends Exception {
    public BibtexFileExceptions(){}

    /**
     * Method requires 1 String element. This element will be printed while throwing an exception
     * @param message
     */
    public BibtexFileExceptions(String message){
        super(message);
    }
}
