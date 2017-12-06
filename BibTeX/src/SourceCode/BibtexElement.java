package SourceCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that contains all basic information about a certain positions
 * Has methods for adding, updating and validating data
 */
public class BibtexElement {
    private String key;
    private String kind;
    private Map<String,String> data;
    private String previousCategory;
    private BibtexValidator validator;

    /**
     * Constructs an BibtexElement with kind, key and maps for validating and storing data
     * @param key String key of a position
     * @param kind String kind of a position
     * @param validatorMap Map used to validate strings, it contains aliases
     * @param requiedFields Map used to validate string containing required fields
     */
    public BibtexElement(String key,String kind,Map<String,String> validatorMap,Map<String,String[]> requiedFields){
        this.kind = kind.toLowerCase().trim();
        this.key = key.trim();
        this.data = new HashMap<>();
        this.validator = new BibtexValidator(validatorMap,requiedFields);
    }

    public String getKey() {
        return key;
    }

    public String getKind(){
        return kind;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, String> getData() {
        return data;
    }

    public String getPreviousCategory(){
        return this.previousCategory;
    }

    /**
     * Method responsible for adding information to element
     * Also while adding a new information it calls the validator to check a previous one
     * @param category Key for a category to be updated
     * @param value Value associated with key
     * @throws BibtexFileExceptions
     */
    public void addInformationToData(String category, String value) throws BibtexFileExceptions {
        if(previousCategory!= null) this.data.put(this.previousCategory,this.validator.validateString(this.previousCategory,data.get(previousCategory)));
        category = category.trim();
        this.data.put(category,value.trim());
        this.previousCategory = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BibtexElement other = (BibtexElement) o;
        if(!key.equals(other.getKey())) return false;
        if(!kind.equals(other.getKind())) return false;
        for(Map.Entry<String, String> iterator:data.entrySet()){
            if(!other.getData().containsKey(iterator.getKey()) || !other.getData().containsValue(iterator.getValue())){
                return false;
            }
        }
        for(Map.Entry<String,String>iterator : other.getData().entrySet()){
            if(!this.data.containsKey(iterator.getKey()) || !this.data.containsValue(iterator.getValue())){
                return false;
            }
        }
        return true;

    }

    public void updateInformationToData(String category, String value){
        data.put(category, data.get(category)+value); //override
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("kind \t"+kind+"\n");
        for(Map.Entry<String, String> iterator : data.entrySet()){
            sb.append(iterator.getKey())
            .append("\t")
            .append(iterator.getValue())
            .append("\n");
        }

        return sb.toString();
    }

    /**
     * Due to the fact, that methods are validated when next one is added we have to validate the last.
     * @throws BibtexFileExceptions
     */
    public void validateLast() throws BibtexFileExceptions {
        this.data.put(this.previousCategory,this.validator.validateString(this.previousCategory,data.get(previousCategory)));
        this.validator.checkRequiedFields(this);
    }
}
