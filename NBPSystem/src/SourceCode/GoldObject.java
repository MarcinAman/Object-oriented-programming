package SourceCode;

import java.lang.*;

/**
 * Class contains all information about certain gold value at time
 */
public class GoldObject {
    String date;
    Double value;

    public GoldObject(String date, Double value){
        this.date = date;
        this.value = value;
    }

    public double getValue(){
        return this.value;
    }

    public String getDate(){
        return this.date;
    }

    public boolean equals(Object other){
        if(!(other instanceof GoldObject)){
            return false;
        }
        GoldObject otherGoldObj = (GoldObject)other;
        if(!otherGoldObj.getDate().equals(this.getDate())){
            return false;
        }
        if(otherGoldObj.getValue()!=this.getValue()){
            return false;
        }
        return true;
    }


}
