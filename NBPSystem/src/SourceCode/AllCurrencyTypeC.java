package SourceCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;

public class AllCurrencyTypeC implements ICurrency {
    public HashSet<CurrencyObject> currencyData;
    public String date;

    public AllCurrencyTypeC(String date) {
        this.currencyData = new HashSet<>();
        this.date = date;
    }

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
