package SourceCode;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DrivingClass {
    String apiResponse;
    private static int valueLength = 20;
    SimpleDateFormat format;

    public DrivingClass (){
        format = new SimpleDateFormat("yyyy-MM-dd");
    }

    //Common part:
    public String getAllData(String URLs) throws IOException {
        URL url = new URL(URLs);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputString;
        StringBuilder api = new StringBuilder();
        while((inputString=input.readLine())!=null){
            api.append(inputString);
        }
        input.close();
        return api.toString();
    }

    //Currency part:

    public ICurrency loadCurrencyData(String day,String table,String column) throws IOException, JSONException {
        ICurrency currency = null;
        if(table.equals("A")||table.startsWith("A")){
            currency =  new AllCurrencyTypeA(day);
        }
        else{
            currency =  new AllCurrencyTypeC(day);
        }
        this.apiResponse = getAllData("http://api.nbp.pl/api/exchangerates/"+column+"/"+table+"/"+day+"/?format=json");
        currency.getAllCurrency(this.apiResponse);
        return currency;
    }

    public String gatherDayCurrencyDataTableA(String currencyName,String day) throws IOException, JSONException {
        ICurrency object = loadCurrencyData(currencyName,"A","rates");
        AllCurrencyTypeA currencyTypeA = (AllCurrencyTypeA) object;
        return "Currency value: " + currencyTypeA.toString();
    }

    public ICurrency loadDayCurrencyDataTableC(String day) throws IOException, JSONException {
        ICurrency currency = loadCurrencyData(day,"C","tables");
        return currency;
    }

    public String getMinAskCurrency(String day) throws IOException, JSONException {
        ICurrency object = loadDayCurrencyDataTableC(day);
        AllCurrencyTypeC currencyTypeC = (AllCurrencyTypeC) object;
        CurrencyObject result = currencyTypeC.getMinCurrencyObject();
        return "Minimal ask currency object is: " + result.toString();
    }

    public String getMaxDiffrenceCurrencies(String day, int amount) throws IOException, JSONException {
        ICurrency iCurrency = loadDayCurrencyDataTableC(day);
        AllCurrencyTypeC object = (AllCurrencyTypeC)iCurrency;
        StringBuilder sb = new StringBuilder();
        if(amount>=object.currencyData.size()){
            sb.append("Not enough currencies. Returning values for top "+(object.currencyData.size()-1)+"\n");
            amount = object.currencyData.size()-1;
        }
        int index = 1;
        while(amount>=0){
            CurrencyObject maxDiff = object.getMaxDiffrenceCurrency();
            if(index>=10){
                sb.append(index).append(". ").append(maxDiff.toDiffrenceString(valueLength-2)).append("\n");
            }
            else{
                sb.append(index).append(". ").append(maxDiff.toDiffrenceString(valueLength)).append("\n");
            }
            amount--;
            index++;
        }
        return sb.toString();
    }

    //https://stackoverflow.com/questions/2186931/java-pass-method-as-parameter

    public String getMaxMinOfCurrency(String currency, String dateSince, String dateTo) throws ParseException, IOException, JSONException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar dateSinceParsed = Calendar.getInstance();
        Calendar dateToParsed = Calendar.getInstance();
        dateSinceParsed.setTime(format.parse(dateSince));
        dateToParsed.setTime(format.parse(dateTo));
        boolean flag = true;
        AllCurrencyTypeA currencyTypeA;
        CurrencyObject minValue = null, maxValue=null;
        while(!dateSinceParsed.equals(dateToParsed) && flag){
            Calendar dateSinceAdded = Calendar.getInstance();
            dateSinceAdded.setTime(dateSinceParsed.getTime());
            dateSinceAdded.add(Calendar.DATE,180);
            if(dateSinceAdded.after(dateToParsed)||dateSinceAdded.equals(dateToParsed)){
                ICurrency iCurrency = loadCurrencyData(format.format(dateSinceParsed.getTime())+"/"+dateTo,
                        "A/"+currency,"rates");
                currencyTypeA = (AllCurrencyTypeA) iCurrency;
                flag = false;
            }
            else{
                ICurrency iCurrency = loadCurrencyData(format.format(dateSinceParsed.getTime())+"/"+
                        format.format(dateSinceAdded.getTime()),
                        "A/"+currency,"rates");
                currencyTypeA = (AllCurrencyTypeA) iCurrency;
                dateSinceParsed = dateSinceAdded;

            }
            CurrencyObject currentMin = currencyTypeA.getMinCurrencyObject();
            if(minValue==null || minValue.getValueAsk()>currentMin.getValueAsk()){
                minValue = currentMin;
            }
            CurrencyObject currentMax = currencyTypeA.getMaxCurrencyObject();
            if(maxValue == null || maxValue.getValueAsk()<currentMax.getValueAsk()){
                maxValue = currentMax;
            }
        }

        return "Min value: " + minValue.toStringWithDate() + "\n" +
                "Max value: " +maxValue.toStringWithDate();
    }

    public String getMaxAplitudeCurrency(String dateSince, String dateTo) throws ParseException, IOException, JSONException {
        //Wczytujemy okresami
        //Jesli nie ma w hashmapie to dodajemy
        //Jesli jest to do value dodajemy abs roznicy pomiedzy ValueAsk z klucz a aktualna.

        HashMap<CurrencyObject,Double>amplitudes = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar dateSinceParsed = Calendar.getInstance();
        Calendar dateToParsed = Calendar.getInstance();
        dateSinceParsed.setTime(format.parse(dateSince));
        dateToParsed.setTime(format.parse(dateTo));
        boolean flag = true;
        while(!dateSinceParsed.equals(dateToParsed) && flag){
            AllCurrencyTypeA currencyTypeA = null;
            Calendar dateSinceAdded = Calendar.getInstance();
            dateSinceAdded.setTime(dateSinceParsed.getTime());
            dateSinceAdded.add(Calendar.DATE,90);
            if(dateSinceAdded.after(dateToParsed) || dateSinceAdded.equals(dateToParsed)){
                ICurrency icurrency = loadCurrencyData(format.format(dateSinceParsed.getTime())+
                        "/"+dateTo,"A","tables");
                currencyTypeA = (AllCurrencyTypeA) icurrency;
                flag = false;
            }
            else{
                ICurrency icurrency = loadCurrencyData(format.format(dateSinceParsed.getTime())+
                        "/"+format.format(dateSinceAdded.getTime()),"A","tables");
                currencyTypeA = (AllCurrencyTypeA) icurrency;
                dateSinceParsed = dateSinceAdded;
            }

            for(CurrencyObject j : currencyTypeA.currencyData){
                boolean isAdded = false;
                for(Map.Entry<CurrencyObject,Double> iterator : amplitudes.entrySet()){
                    if(j.equals(iterator.getKey())){
                        Double difference = iterator.getKey().calculateDiffrenceInAskValue(j)+iterator.getValue();
                        iterator.getKey().setValueSell(j.getValueAsk());
                        CurrencyObject i = iterator.getKey();
                        amplitudes.remove(iterator);
                        amplitudes.put(i,difference);
                        isAdded = true;
                    }
                }
                if(!isAdded){
                    amplitudes.put(j,0.0);
                }
            }
        }
        Double maxValue = null;
        CurrencyObject maxCurrency = null;
        for(Map.Entry<CurrencyObject,Double> i : amplitudes.entrySet()){
            if(maxValue==null || maxValue.compareTo(i.getValue())==0){
                maxValue = i.getValue();
                maxCurrency = i.getKey();
            }
        }
        return "Max amplitude has currency: " + maxCurrency.getFullName() + " with: " + maxValue;
    }

    public Calendar parseDateString(String date) throws ParseException {
        Calendar dateParsed = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        dateParsed.setTime(format.parse(date));
        return dateParsed;
    }

    public String makeBarChart(Double value, Double div,Character baseChar){
        Double times = value;
        if(div!=0) times = value/div;
        return String.join("", Collections.nCopies(times.intValue(),baseChar.toString()));
    }

    public String drawWeeklyChart(String dateSince, String dateTo, String currency,Character baseChar) throws ParseException, JSONException {
        //Jaki jest dokladnie input?
        String monthDateSince = dateSince.substring(0,dateSince.lastIndexOf("-"))+"-01";
        int weekNumberSince = Integer.parseInt(dateSince.substring(dateSince.lastIndexOf("-")+1,dateSince.length()));
        String monthDateTo = dateTo.substring(0,dateTo.lastIndexOf("-"))+"-01";
        int weekNumberTo = Integer.parseInt(dateTo.substring(dateTo.lastIndexOf("-")+1,dateTo.length()));
        Calendar dateSinceParsed = parseDateString(monthDateSince);
        dateSinceParsed.set(Calendar.WEEK_OF_MONTH,weekNumberSince);
        Calendar dateToParsed = parseDateString(monthDateTo);
        dateToParsed.set(Calendar.WEEK_OF_MONTH,weekNumberTo);
        while(dateSinceParsed.get(Calendar.DAY_OF_WEEK)!=2) dateSinceParsed.add(Calendar.DATE,1);
        StringBuilder sb = new StringBuilder();
        for(Calendar j = dateSinceParsed;j.get(Calendar.DAY_OF_WEEK)!=7;j.add(Calendar.DATE,1)){
            Calendar i = Calendar.getInstance();
            for(i.set(j.get(Calendar.YEAR),j.get(Calendar.MONTH),j.get(Calendar.DAY_OF_MONTH));i.before(dateToParsed);i.add(Calendar.DATE,7)) {
                ICurrency icurrency = null;
                try {
                    icurrency = loadCurrencyData(format.format(i.getTime()),"A/"+currency,"rates");
                    AllCurrencyTypeA currencyTypeA = (AllCurrencyTypeA) icurrency;
                    sb.append(i.get(Calendar.DAY_OF_WEEK) +". "+makeBarChart(currencyTypeA.getFirstValue(),0.1,baseChar)+"\n");
                } catch (IOException e) {
                    sb.append(i.get(Calendar.DAY_OF_WEEK)+". No data \n");
                }
            }
        }
        return sb.toString();
    }

    //SourceCode.Gold Part:

    public Gold loadDayGoldData(String day) throws IOException, JSONException {
        Gold gold = new Gold(day);
        this.apiResponse = getAllData("http://api.nbp.pl/api/cenyzlota/"+day+"/?format=json");
        gold.loadGoldRates(this.apiResponse);
        return gold;
    }

    public String gatherGoldData(String day) throws Exception {
        Gold object =  this.loadDayGoldData(day);
        return object.toString();
    }

    public String gatherAverageGoldPrice(String dateSince, String dateTo) throws IOException, JSONException, ParseException {
        if(dateSince.substring(0,7).equals(dateTo.substring(0,7))){
            Gold gold = loadDayGoldData(dateSince+"/"+dateTo);
            return "Avg gold price in period: " + dateSince +" to " + dateTo + " is "+gold.getAvgFromPeriod();
        }
        else{
            BigDecimal sum = new BigDecimal(0,new MathContext(2));
            BigDecimal days = new BigDecimal(0);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar dateSinceParsed = Calendar.getInstance();
            Calendar dateToParsed = Calendar.getInstance();
            dateSinceParsed.setTime(format.parse(dateSince));
            dateToParsed.setTime(format.parse(dateTo));

            while(!dateSinceParsed.equals(dateToParsed)){
                Calendar dateSinceAdded = Calendar.getInstance();
                dateSinceAdded.setTime(dateSinceParsed.getTime());
                dateSinceAdded.add(Calendar.DATE,50);
                if(dateSinceAdded.after(dateToParsed) || dateSinceAdded.equals(dateToParsed)){
                    Gold gold = loadDayGoldData(format.format(dateSinceParsed.getTime())+"/"+dateTo);
                    sum = sum.add(gold.getSumFromPeriod());
                    days = days.add(gold.getDaysAmount());
                    break;
                }
                else{
                    Gold gold = loadDayGoldData(format.format(dateSinceParsed.getTime())+"/"+
                    format.format(dateSinceAdded.getTime()));
                    sum = sum.add(gold.getSumFromPeriod());
                    days = days.add(gold.getDaysAmount());
                    dateSinceParsed = dateSinceAdded;
                }
            }
            BigDecimal result = sum.divide(days,2, RoundingMode.HALF_EVEN);
            return "Avg gold price in period: " + dateSince +" to " + dateTo + " is "+result;
        }
    }

}
