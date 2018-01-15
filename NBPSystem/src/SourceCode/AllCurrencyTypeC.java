package SourceCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

public class AllCurrencyTypeC implements ICurrency {
    public ArrayList<CurrencyObject> currencyData;
    public String date;

    public AllCurrencyTypeC(String date) {
        this.currencyData = new ArrayList<>();
        this.date = date;
    }

    /**
     * * To this method we can pass an JSON array. It will parse it into a object form (Currency Object) and add to HashSet
     *
     * Be aware that Hashset does NOT keep the chronology between added elements as it uses a Hash function to define position
     * If you want to have a chronology you might consider 2 approches:
     * 1)Changing it to LinkedList
     * 2)Passing a api response consisting only of 1 day
     *
     * As second solution is slower i suggest using it only if you have a limited time period
     *
     * @param apiResponse String consisting of api response
     * @throws JSONException Thrown if apiResponse is in wrong format
     */
    @Override
    public void getAllCurrency(String apiResponse) throws JSONException {
        JSONArray jsonArray = new JSONArray(apiResponse);
        for(int i=0;i<jsonArray.length();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            JSONArray arr = jsonObject.getJSONArray("rates");
            for(int j = 0;j<arr.length();j++){
                JSONObject currentObject = arr.getJSONObject(j);
                currencyData.add(
                        new CurrencyObject((String)currentObject.get("currency"),
                                (String)currentObject.get("code"),
                                (Double)currentObject.get("bid"),
                                (Double)currentObject.get("ask"))
                );
            }
        }
    }

    /**
     * Getting an CurrencyObject with minimal as price in currencyData
     * @return CurrencyObject
     */
    public CurrencyObject getMinCurrencyObject(){

        CurrencyObject minValue = new CurrencyObject(null,null, Double.MAX_VALUE,Double.MAX_VALUE);
        for(CurrencyObject i : currencyData){
            if(i.getValueAsk()<=minValue.getValueAsk()){
                minValue = i;
            }
        }
        if(minValue.getValueAsk()==Double.MAX_VALUE){ //Exception?
            return null;
        }
        return minValue;
    }

    /**
     * Method returns an CurrencyObject which has the highest diffrence value between ask and bid price.
     * Waring: after returning this value certain object is removed from hashset!
     * @return CurrencyObject with highest value
     */
    public CurrencyObject getMaxDiffrenceCurrency(){
        CurrencyObject maxDiff = null;
        for(CurrencyObject i : currencyData){
            if(maxDiff == null || maxDiff.calculateDiffrenceInPrice()<i.calculateDiffrenceInPrice()){
                maxDiff = i;
            }
        }
        currencyData.remove(maxDiff);
        return maxDiff;
    }

}
