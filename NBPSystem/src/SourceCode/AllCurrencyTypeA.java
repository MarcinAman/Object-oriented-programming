package SourceCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

public class AllCurrencyTypeA implements ICurrency {
    public ArrayList<CurrencyObject> currencyData;
    String date;

    public AllCurrencyTypeA(String date){
        this.date = date;
        this.currencyData = new ArrayList<>();
    }

    /**
     * To this method we can pass an JSON array or JSON object. It will parse it into a object form (Currency Object) and add to ArrayList
     *
     * @param apiResponse String consisting of api response
     * @throws JSONException Thrown if apiResponse is in wrong format
     */
    public void getAllCurrency(String apiResponse) throws JSONException {
        if(apiResponse.startsWith("[")){
            JSONArray jsonArray = new JSONArray(apiResponse);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                JSONArray arr = object.getJSONArray("rates");
                for(int j = 0;j<arr.length();j++){
                    JSONObject current = arr.getJSONObject(j);
                    currencyData.add(new CurrencyObject(
                            (String)current.get("currency"),
                            (String)current.get("code"),
                            (Double)current.get("mid"),
                            (String)object.get("effectiveDate")
                    ));
                }
            }
        }
        else{
            JSONObject jsonObject = new JSONObject(apiResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("rates");
            for(int i = 0;i<jsonArray.length();i++){
                JSONObject object =  jsonArray.getJSONObject(i);
                currencyData.add(new CurrencyObject(
                        (String)jsonObject.get("currency"),
                        (String)jsonObject.get("code"),
                        (Double)object.get("mid"),
                        (String)object.get("effectiveDate")
                ));
            }
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(CurrencyObject i : currencyData){
            sb.append(i.getFullName())
                    .append("\t")
                    .append(i.getShortName())
                    .append("\t")
                    .append(i.getValueAsk())
                    .append("\n");
        }
        return sb.toString();
    }


    /*
    This method is used only when a currencyData hashset has 1 value since
    there is no way to tell which element will be returned
     */

    /**
     * Method used to get first value in currencyData.
     * Warning: use it only if you are entirely sure that size of currencyData is exactly 1
     * @return Double value of element
     */
    public Double getFirstValue(){
        for(CurrencyObject i : currencyData){
            return i.getValueAsk();
        }
        return null;
    }

    /**
     * Getting an CurrencyObject with maximal as price in currencyData
     * @return CurrencyObject
     */
    public CurrencyObject getMaxCurrencyObject(){
        CurrencyObject maxValue = null;
        for(CurrencyObject i : currencyData){
            if(maxValue == null || maxValue.getValueAsk()<i.getValueAsk()){
                maxValue = i;
            }
        }
        return maxValue;
    }

    /**
     * Getting an CurrencyObject with minimal as price in currencyData
     * @return CurrencyObject
     */
    public CurrencyObject getMinCurrencyObject(){
        CurrencyObject minValue = null;
        for(CurrencyObject i : currencyData){
            if(minValue == null || minValue.getValueAsk()>i.getValueAsk()){
                minValue = i;
            }
        }
        return minValue;
    }

    /**
     * Method returns an ask value of certain currency
     * @param currencyName String name with short symbol (3 characters long) of currency
     * @return Double value
     */
    public Double getCurrencyAskValue(String currencyName){
        for(CurrencyObject i : currencyData){
            if(i.getShortName().equals(currencyName)){
                return i.getValueAsk();
            }
        }
        return null;
    }
}
