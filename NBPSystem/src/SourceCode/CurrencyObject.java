package SourceCode;

/**
 * Class in which you can find all information about currency in certain day.
 */
public class CurrencyObject {
    public String fullName;
    public String shortName;
    public Double valueSell;
    public Double valueBid;
    public String date;

    public CurrencyObject(String fullName, String shortName, Double value,String date){
        this.fullName =fullName;
        this.shortName = shortName;
        this.valueSell = value;
        this.valueBid = null;
        this.date = date;
    }

    public CurrencyObject(String fullName, String shortName, Double valueBid, Double valueAsk){
        this.fullName =fullName;
        this.shortName = shortName;
        this.valueBid = valueBid;
        this.valueSell = valueAsk;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setValueBid(Double value){
        this.valueBid = value;
    }

    public Double getValueBid(){
        return this.valueBid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Double getValueAsk() {
        return valueSell;
    }

    public String getDate(){
        return this.date;
    }

    public double getGenericValueAsk(){
        return valueSell.doubleValue();
    }

    public void setValueSell(Double value) {
        this.valueSell = value;
    }

    public Double calculateDiffrenceInPrice(){
        return valueSell-valueBid;
    }

    public Double calculateDiffrenceInAskValue(CurrencyObject other){
       return Math.abs(other.getValueAsk()-this.getValueAsk());
    }

    @Override
    public String toString(){
        return this.fullName+"\t"+this.shortName+"\t"+this.valueSell;
    }

    //https://dzone.com/articles/java-string-format-examples

    public String toDiffrenceString(int valueLenght){
        return String.format("%-"+valueLenght+"."+valueLenght+"s",this.fullName)+"\t"+this.shortName+"\t"+
                String.format("%.5f",this.valueBid)+"\t"+String.format("%.5f",this.valueSell)+
                "\t"+String.format("%.6f",calculateDiffrenceInPrice());
    }

    public String toStringWithDate(){
        return this.fullName + "\t" + this.date + "\t" +this.valueSell;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurrencyObject)) return false;

        CurrencyObject that = (CurrencyObject) o;

        return getShortName().equals(that.getShortName());
    }
}
