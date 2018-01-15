package SourceCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;


public class Gold{
    public ArrayList<GoldObject> prices;
    public String day;

    public Gold(String day){
        this.prices = new ArrayList<>();
        this.day = day;
    }

    /**
     * Method takes a String with api response and parses it into object form (GoldObjects). After that objects are added to hashset.
     *
     * @param apiResponse String consisting of api response
     * @throws JSONException Thrown if apiResponse is in wrong format
     */
    public void loadGoldRates(String apiResponse) throws JSONException {
        JSONArray jsonArray = new JSONArray(apiResponse);
        for(int i = 0;i<jsonArray.length();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            prices.add(new GoldObject((String)jsonObject.get("data"),(Double)jsonObject.get("cena")));
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("Current gold value: ");
        for(GoldObject i : prices){
            sb.append(i.getValue()).append("\n");
        }
        return sb.toString();
    }

    public String getDay(){
        return this.day;
    }

    /**
     * Method sums all values that are in hashset and returns it in BigDecimal form
     * @return BigDecimal with sum value
     */
    public BigDecimal getSumFromPeriod(){
        BigDecimal pricesSum = new BigDecimal(0);
        for(GoldObject i : prices){
            BigDecimal added = new BigDecimal(i.getValue());
            pricesSum = pricesSum.add(added);
        }
        return pricesSum;
    }

    public BigDecimal getDaysAmount(){
        return new BigDecimal(prices.size());
    }

    public BigDecimal getAvgFromPeriod(){
        return getSumFromPeriod().divide(getDaysAmount(),2, RoundingMode.HALF_EVEN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gold)) return false;
        Gold gold = (Gold) o;

        //Not the best way to compare hashsets but it gets the job done

        int flags = 0;
        for(GoldObject i : prices){
            for(GoldObject j : gold.prices){
                if(i.equals(j)){
                    flags++;
                }
            }
        }
        if(prices.size()!=gold.prices.size() || flags!=prices.size()){
            return false;
        }
        return true;
    }
}
