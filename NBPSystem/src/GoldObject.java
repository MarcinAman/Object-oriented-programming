import java.lang.*;

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


}
