package SourceCode;

import java.util.Map;

/**
 * Class responsible for validating an element
 */
public class BibtexValidator {
    Map<String,String> aliases;
    Map<String,String[]> requiedField;

    /**
     * Constructs an empty element with aliases that are known till this point and required fields for all positions
     * @param aliases a Map containing all, known aliases
     * @param requiedField a Map containing requied fields for all positions
     */
    public BibtexValidator(Map<String, String> aliases,Map<String,String[]>requiedField){
        this.aliases = aliases;
        this.requiedField =requiedField;
    }

    /**
     * Method responsible for removing opening and closing brackets
     * @param record String that we want to remove brackets from
     * @return String without brackets
     * @throws BibtexFileExceptions Thrown is a input contains only opening bracket but not closing one
     */
    public String removeBrackets(String record) throws BibtexFileExceptions {
        record = record.trim();
        String recordReversed = new StringBuilder(record).reverse().toString();
        int indexFirst = record.indexOf('{');
        int indexLast = recordReversed.indexOf('}');
        if(indexFirst==0){
            if(indexLast<0){
                throw new BibtexFileExceptions("Wrong file format, missing { }");
            }
            else{
                return record.substring(indexFirst+1,record.length()-indexLast-1);
            }
        }
        return record;
    }

    /**
     * Method responsible for removing opening and closing quotes
     * @param record String that we want to remove quotes from
     * @return String without quotes
     * @throws BibtexFileExceptions Thrown is a input contains only opening quote but not closing one
     */
    public String removeQuotes(String record) throws BibtexFileExceptions {
        record = record.trim();
       int firstIndex = record.indexOf('"');
       int finishingIndex = record.lastIndexOf('"');
       int hasBracket = record.indexOf('{');
       if(firstIndex == 0 || (firstIndex < hasBracket && hasBracket >=0 && firstIndex >=0)){
           if(finishingIndex<=0) throw new BibtexFileExceptions("Wrong file format, missing \" " );
           StringBuilder sb = new StringBuilder(record);
           sb.setCharAt(firstIndex,' ');
           sb.setCharAt(finishingIndex,' ');
           return sb.toString().trim();
       }
       return record;
    }

    /**
     * Validating a name field to a correct format which is name-surname
     * @param value
     * @return Validated String
     */
    public String checkNameField(String value){
        if(value.contains(",")){
            int bracketIndex = value.indexOf("{");
            int dotIndex = value.indexOf(",");
            if(bracketIndex==-1 || bracketIndex > dotIndex){
                int pivot = value.indexOf(",");
                String name = value.substring(pivot+1,value.length());
                String surname = value.substring(0,pivot);
                value = name + " " + surname;
            }
        }
        return value.trim();
    }

    /**
     * Changing substrings that are not in brackets and contain a substring that is aliased into a full form
     * @param record Input string to be changed
     * @param key Key value to be changed
     * @param value Value to change the key substring into
     * @return String with aliases
     */
    public String changeAlias(String record, String key, String value){
        if(record.contains(key)){
            int positionKey = record.indexOf(key);
            int positionOpening = record.indexOf('{');
            int positionClosing = record.indexOf('}');
            if(positionKey >= positionOpening && positionClosing >= positionKey){
                // example: key = some, value = more
                // {Some} value -> {Some} + changeAlias("value")
                return record.substring(0,positionClosing)+
                        changeAlias(record.substring(positionClosing,record.length()),key,value);
            }
            else if(positionClosing==-1 || positionOpening == -1){
                // Some value -> more value
                return record.replaceAll(key,value);
            }
            else{
                // value Some Some value -> value more more value
                int endOfKey = positionKey+key.length();
                return record.substring(0,positionKey)+record.substring(positionKey,endOfKey).replaceAll(key,value)+
                        changeAlias(record.substring(endOfKey,record.length()),key,value);
            }
        }
        return record;
    }

    public String addAliases(String record){
        for(Map.Entry<String,String>iterator : aliases.entrySet()){
            record = changeAlias(record,iterator.getKey(),iterator.getValue());
        }
        return record;
    }

    /**
     * Checking requied fields for specific elements. It searches for them in requiedField mMap
     * @param element Element to be validated
     * @throws BibtexFileExceptions
     */
    public void checkRequiedFields(BibtexElement element) throws BibtexFileExceptions{
        if(requiedField == null){
            return; //Just for testing and aliases
        }
        for(Map.Entry<String,String[]>requiedIterator:requiedField.entrySet()){
            if(element.getKind().equals(requiedIterator.getKey().toLowerCase())){
                if(element.getData().containsKey("crossref")){
                    return; //Case it is a crossref we pass it because main element will be validated
                }
                if(element.getData().containsKey("journal")){
                    return;
                }
                if(element.getKind().equals("book")||element.getKind().equals("inbook")){
                    if(!element.getData().containsKey("author") && !element.getData().containsKey("editor")){
                        System.out.println(element);
                        throw new BibtexFileExceptions("Lack of editor/ author field");
                    }
                }
                if(element.getKind().equals("inbook") && !element.getData().containsKey("chapter") && !element.getData().containsKey("pages")){
                    System.out.println(element);
                    throw new BibtexFileExceptions("Lack of chapters/pages field");
                }
                for(String a : requiedIterator.getValue()){
                    if(!element.getData().containsKey(a)){
                        System.out.println(element); //left for sake of knowing which elements are not complete
                        throw new BibtexFileExceptions("Lack of "+a+" field");
                    }
                }
            }
        }
    }


    /**
     * Method validates a input String. It calls other methods like :
     * -removeBrackets
     * -removeQuotes
     * -addAliases
     * Input string has it's brackets/quotes removed and if it has only quotes aliases are added
     * @param record String to be validated
     * @return Validated String
     * @throws BibtexFileExceptions
     */
    public String validateString(String category,String record) throws BibtexFileExceptions {
        if(record == null) return null;
        record = record.trim();
        String currentlyValidated = removeBrackets(record);
        if(!currentlyValidated.equals(record)){
            return currentlyValidated;
        }
        else{
            currentlyValidated = removeQuotes(currentlyValidated);
        }

        if(category.equals("author") || category.equals("editor")){
            if(record.contains(",")) currentlyValidated = checkNameField(currentlyValidated);
        }
        if(aliases!=null){
            currentlyValidated = addAliases(currentlyValidated);
        }
        return currentlyValidated;
    }
}
