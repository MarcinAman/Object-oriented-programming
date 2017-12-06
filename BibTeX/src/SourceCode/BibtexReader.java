package SourceCode;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Reader class responsible for opening file, getting data and closing it
 */

public class BibtexReader {

    private LinkedList<BibtexElement> elements;
    private boolean isInBlock;
    private Map<String,String> aliases;
    private BibtexValidator validator;
    private Map<String, String []> requiedFields;

    /**
     * Constructor creates all fields such as LinkedList of BibtexElement or Map with aliases.
     * Also calls a method, that creates fields that are required in specific kinds of elements
     */
    public BibtexReader(){
        this.elements=new LinkedList<>();
        this.isInBlock = false;
        this.aliases = new HashMap<>();
        this.validator = new BibtexValidator(null,null); //Validator used only for aliases
        this.requiedFields = new HashMap<>();
        this.initializeReqiedFields();
    }

    /**
     * Method checks for the file from input and tries to open it.
     * @param fileName Method takes a string that contains file name and path
     * @return Method returns a BufferedReader object if opening a file is successful, otherwise an exception is thrown
     * @throws FileNotFoundException
     */

    public BufferedReader prepareFile(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        return reader;
    }

    /***
     *   Method performs operations on the string that is taken from file.
     *   Operations include validating, recognising specific fields of text and adding it to the list
     *   @param bufferedReader  Method takes a BufferedReader object that is connected with an opened file
     *
   */
    public void performFileOperation(BufferedReader bufferedReader) throws IOException, BibtexFileExceptions {
        String line = null;
        while((line=bufferedReader.readLine())!=null){
            operateOnString(line);
        }
    }


    /**
     * Function is dividing a string to diffrent categories based on its the type and positon in text
     * it is also responsible for adding a new elements and updating information
     * @param line Method takes a string that is exactly 1 line of file.
     * @throws BibtexFileExceptions
     */
    public void operateOnString(String line) throws BibtexFileExceptions{
        if(line.length()>=1 && line.charAt(0)=='@') {
            if(line.toLowerCase().startsWith("@string")){
                line = line.substring(8,line.length()-1);
                int indexEquals = line.indexOf("=");
                if(indexEquals < 0) throw new BibtexFileExceptions("Wrong alias configuration");
                String key = line.substring(0,indexEquals);
                String value = line.substring(indexEquals+1,line.length());
                if(key == null || value == null) throw new BibtexFileExceptions("Wrong alias configuration");
                aliases.put(key.trim(),validator.validateString(key.trim(),value.trim()).trim());
            }
            else if(!line.toLowerCase().startsWith("@preamble")){
                isInBlock = true;
                if(elements.size()>0) elements.get(elements.size()-1).validateLast();
                elements.add(generateNewBibtexElement(line));
            }
        }
        else if(line.length()>=1 && line.charAt(0)=='}'){
            isInBlock = false;
        }
        else if(isInBlock){
            addInformationToElements(line);
        }
        //else it is a comment so we pass it
    }

    /**
     * Method is responsible for adding information to a element that is currently last in elements container
     * @param line Takes a line of file input and divides it into a category key and data.
     * @throws BibtexFileExceptions
     */
    private void addInformationToElements(String line) throws BibtexFileExceptions {
        int ind = line.indexOf('=');
       // System.out.println(line + " "+ ind);
        if(ind < 0 || ind > line.length()){
            String category = elements.getLast().getPreviousCategory();
            elements.getLast().updateInformationToData(category,line);
        }
        else{
            String category = line.substring(0,ind);
            String value = line.substring(ind+1,line.length()-1);
            elements.getLast().addInformationToData(category,value);
        }
    }

    /**
     * Method generates a new BibtexElement based on a input line and is responsible of dividing
     * it into category key and data
     * case a line doesnt contain a " sign an exception is thrown
     * @param line Exactly 1 line of file input
     * @return BibtexElement
     * @throws BibtexFileExceptions
     */
    public BibtexElement generateNewBibtexElement(String line) throws BibtexFileExceptions{
        String key = null;
        String kind = null;
        int index = line.indexOf('{');
        key = line.substring(index+1,line.length()-1);
        kind = line.substring(1,index);
//        System.out.println(kind + " " + key + " "+index);
        if(kind == null || key == null){
            throw new BibtexFileExceptions("File format error");
        }
        return new BibtexElement(key, kind,aliases,this.requiedFields);
    }

    /**
     * After reading whole file it has to be closed. This method is responsible for that
     * @param bufferedReader
     * @throws IOException
     */

    public void closeFile(BufferedReader bufferedReader) throws IOException {
        bufferedReader.close();
    }

    /**
     * If it is called with no arguments it is assumed that the user wants to print all data currently available.
     * @return String with whole data available at the moment in element
     */
    @Override //classical version of toString
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(BibtexElement i : elements){
            sb.append(i);
        }
        return sb.toString();
    }

    private StringBuilder addSigns(StringBuilder sb, String sign,int times){
        for(int i = 0;i<times;i++){
            sb.append(sign);
        }
        return sb.append("\n");
    }

    private String toString(String sign){ //version with argument
        StringBuilder sb = new StringBuilder();
        for(BibtexElement i : elements){
            sb = addSigns(sb,sign,50);
            sb.append(i);
        }
        sb = addSigns(sb,sign,50);
        return sb.toString();
    }

    /**
     * Method creates a string with a information about elements that contains a phrase.
     * The phrase can be a category key, filed, substring of a field or a kind
     * @param key Value that will be searched by
     * @param sign Sign with an input will be created
     * @param field Field to be searched in
     * @return String with records of BibtexElement containing information that contains a phrase
     */
    public String toString(String key, String sign, String field){
        if(key.equals("all") && field.equals("all")){
            return toString(sign);
        }
        StringBuilder sb = new StringBuilder();
        if(field.equals("all")){
            for(BibtexElement i : elements){
                if(i.getData().containsValue(key)||i.getData().containsKey(key)||i.getKey().equals(key)||i.getKind().equals(key)){
                    sb = addSigns(sb,sign,20);
                    sb.append(i);
                }
            }
            return sb.toString();
        }
        if(field.equals("kind")){
            for(BibtexElement i : elements){
                if(i.getKind().trim().equals(key)){
                    sb = addSigns(sb,sign,20);
                    sb.append(i);
                }
            }
            return sb.toString();
        }
        if(field.equals("key")){
            for(BibtexElement i : elements){
                if(i.getKey().trim().equals(key)){
                    sb = addSigns(sb,sign,20);
                    sb.append(i);
                }
            }
            return sb.toString();
        }
        for(BibtexElement i : elements){
            if(i.getData().containsKey(field)){
                String val = (String) i.getData().get(field);
                if(val.equals(key) || val.contains(key) || key.contains(val)){
                    sb = addSigns(sb,sign,20);
                    sb.append(i);
                }
            }
        }
        return sb.toString();
    }

    private void initializeReqiedFields(){
        requiedFields.put("article", new String[]{"author", "title", "journal", "year"});
        requiedFields.put("book",new String[]{"title", "publisher", "year"}); //+author / editor
        requiedFields.put("inproceedings",new String[]{"author", "title", "booktitle", "year"});
        requiedFields.put("conference",new String[]{"author", "title", "booktitle", "year"});
        requiedFields.put("booklet",new String[]{"title"});
        requiedFields.put("inbook",new String[]{"title", "publisher", "year"}); //+author / editor oraz chapter/pages
        requiedFields.put("incollection",new String[]{"author", "title", "booktitle", "publisher", "year"});
        requiedFields.put("manual",new String[]{"title"});
        requiedFields.put("mastersthesis",new String[]{"author", "title", "school", "year"});
        requiedFields.put("phdthesis",new String[]{"author", "title", "school", "year"});
        requiedFields.put("techreport",new String[]{"author", "title", "institution", "year"});
        requiedFields.put("misc",new String[]{}); //lack of requied fields
        requiedFields.put("unpublished",new String[]{"author", "title", "note"});
    }
}
