package SourceCode;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
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

public class DrivingClass extends Application{
    //String apiResponse;
    private static int valueLength = 20;
    public SimpleDateFormat format;
    private static int workingDays = 7;
    public static String [] stringDays = {null,null,"MON","TUE","WED","THU","FRI",null};

    public DrivingClass (){
        format = new SimpleDateFormat("yyyy-MM-dd");
    }


    //Common part:

    /**
     * Method is responsible for sending requests to a api server.
     * String value of the api response is being returned
     * after that connection to a server is closed
     *
     * @param URLs url connection
     * @return String value of api response
     * @throws IOException Thrown if server response is "404"
     */

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
        if(api.toString().startsWith("404")) throw new IOException("Data not found");
//        if(!api.toString().startsWith("[") && !api.toString().startsWith("{")){
//            return getAllData(URLs);
//        }
        return api.toString();
    }

    //Currency part:

    /**
     * Method loading a string parameters info an URL-String form and then parse it into objects
     * Possible return objects are: AllCurrencyTypeA or AllCurrencyTypeC as they implement ICurrency interface
     * @param name Name of currency that we search for
     * @param date Date or a time period in format date1/date2 or just date1
     * @param table Table to search. Can be either A or C
     * @param column In which we search. To exacly know which you want see certain api return methods
     * @return ICurrency Object with data
     * @throws IOException Thrown if server returns 404
     * @throws JSONException If a JSON file returned by server is in wrong format
     */
    public ICurrency loadCurrencyData(String name, String date,String table,String column) throws IOException, JSONException {
        ICurrency currency = null;
        if(table.equals("A")||table.startsWith("A")){
            currency =  new AllCurrencyTypeA(date);
        }
        else{
            currency =  new AllCurrencyTypeC(date);
        }
        if(name==null){
            currency.getAllCurrency(getAllData("http://api.nbp.pl/api/exchangerates/"+column+"/"+table+"/"+date+"/?format=json"));
        }
        else{
            currency.getAllCurrency(getAllData("http://api.nbp.pl/api/exchangerates/"+column+"/"+table+"/"+name+"/"+date+"/?format=json"));

        }
        return currency;
    }

    /**
     * Pass-through method to make your life easier. You don't need to remember the table or column.
     * Those values are set by default to A and rates
     *
     * @param currencyName Name of the currency that you want to search. The short one (3 characters long)
     * @param day Date in format YYYY-MM-DD
     * @return Price of a certain currency in given period
     * @throws IOException Thrown if server returns 404
     * @throws JSONException If a JSON file returned by server is in wrong format
     */

    public String gatherDayCurrencyDataTableA(String currencyName,String day) throws IOException, JSONException {
        ICurrency object = loadCurrencyData(currencyName,day,"A","rates");
        AllCurrencyTypeA currencyTypeA = (AllCurrencyTypeA) object;
        return "Currency value: " + currencyTypeA.toString();
    }

    public ICurrency loadDayCurrencyDataTableC(String day) throws IOException, JSONException {
        ICurrency currency = loadCurrencyData(null,day,"C","tables");
        return currency;
    }

    /**
     * Method returns an information about currency with a minimal price in certain date
     * @param day String value of date in format YYYY-MM-DD
     * @return String with minimal currency value and symbol
     * @throws IOException Thrown if server returns 404
     * @throws JSONException If a JSON file returned by server is in wrong format
     */
    public String getMinAskCurrency(String day) throws IOException, JSONException {
        ICurrency object = loadDayCurrencyDataTableC(day);
        AllCurrencyTypeC currencyTypeC = (AllCurrencyTypeC) object;
        CurrencyObject result = currencyTypeC.getMinCurrencyObject();
        return "Minimal ask currency object is: " + result.toString();
    }

    /**
     * Method returns information about currencies with max difference in price between ask and bid values
     * @param day Day to gather data in format YYYY-MM-DD
     * @param amount Amount of records that you want to have sorted in reverse order. If there is not enough all of them will be returned
     * @return String containing information about currencies with max diffrence in price
     * @throws IOException Thrown if server returns 404
     * @throws JSONException If a JSON file returned by server is in wrong format
     */
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

    /**
     * Method gathers information about max and min price of a certain currency in given period
     * From a programming point of view this period can be infinitely long but NBP Api only has data since 2nd of january 2002
     * @param currency Short (3 characters long) currency symbol
     * @param dateSince Date since you want to have information in format YYYY-MM-DD
     * @param dateTo Date to you want to have information in format YYYY-MM-DD
     * @return String containing information about max-es and min-s of a certain currency
     * @throws ParseException Thrown if a date is different format that YYYY-MM-DD
     * @throws IOException Thrown if server returns 404
     * @throws JSONException If a JSON file returned by server is in wrong format
     */
    public String getMaxMinOfCurrency(String currency, String dateSince, String dateTo) throws ParseException, IOException, JSONException {
        Calendar dateSinceParsed = parseDateString(dateSince);
        Calendar dateToParsed = parseDateString(dateTo);
        boolean flag = true;
        AllCurrencyTypeA currencyTypeA;
        CurrencyObject minValue = null, maxValue=null;
        while(!dateSinceParsed.equals(dateToParsed) && flag){
            Calendar dateSinceAdded = Calendar.getInstance();
            dateSinceAdded.setTime(dateSinceParsed.getTime());
            dateSinceAdded.add(Calendar.DATE,180);
            if(dateSinceAdded.after(dateToParsed)||dateSinceAdded.equals(dateToParsed)){
                ICurrency iCurrency = loadCurrencyData(null,format.format(dateSinceParsed.getTime())+"/"+dateTo,
                        "A/"+currency,"rates");
                currencyTypeA = (AllCurrencyTypeA) iCurrency;
                flag = false;
            }
            else{
                ICurrency iCurrency = loadCurrencyData(null,format.format(dateSinceParsed.getTime())+"/"+
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

    /**
     * Method returns a String with name of a currency that had max aplitude in specific time period.
     *
     * @param dateSince Date since you want to have information in format YYYY-MM-DD
     * @param dateTo Date to you want to have information in format YYYY-MM-DD
     * @return String with currency name
     * @throws ParseException Thrown if a date is different format that YYYY-MM-DD
     * @throws IOException Thrown if server returns 404
     * @throws JSONException If a JSON file returned by server is in wrong format
     */
    public String getMaxAplitudeCurrency(String dateSince, String dateTo) throws ParseException, IOException, JSONException {
        //Wczytujemy okresami
        //Jesli nie ma w hashmapie to dodajemy
        //Jesli jest to do value dodajemy abs roznicy pomiedzy ValueAsk z klucz a aktualna.

        HashMap<CurrencyObject,Double>amplitudes = new HashMap<>();
        Calendar dateSinceParsed = parseDateString(dateSince);
        Calendar dateToParsed = parseDateString(dateTo);
        boolean flag = true;
        while(!dateSinceParsed.equals(dateToParsed) && flag){
            AllCurrencyTypeA currencyTypeA = null;
            Calendar dateSinceAdded = Calendar.getInstance();
            dateSinceAdded.setTime(dateSinceParsed.getTime());
            dateSinceAdded.add(Calendar.DATE,90);
            if(dateSinceAdded.after(dateToParsed) || dateSinceAdded.equals(dateToParsed)){
                ICurrency icurrency = loadCurrencyData(null,format.format(dateSinceParsed.getTime())+
                        "/"+dateTo,"A","tables");
                currencyTypeA = (AllCurrencyTypeA) icurrency;
                flag = false;
            }
            else{
                ICurrency icurrency = loadCurrencyData(null,format.format(dateSinceParsed.getTime())+
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

    public String makeBarChart(Double value, Double div,String baseChar){
        Double times = value;
        if(div!=0) times = value/div;
        return String.join("", Collections.nCopies(times.intValue(),baseChar))+" "+value;
    }

    /**
     * Method draws a graph in terminal representing weekly data about currency.
     * Graph is in format:
     * mondays-tuesedays etc so you can easily compare values between certain weeks
     *
     * @param dateSince Date since you want to have information in format YYYY-MM-DD
     * @param dateTo Date to you want to have information in format YYYY-MM-DD
     * @param currency Short (3 characters long) currency symbol
     * @param baseChar Char to produce a graph with. I highly suggest using sthing nice like + or "\u25A1" which is square as it will be easier to read.
     * @return String with graph
     * @throws ParseException Thrown if a date is different format that YYYY-MM-DD
     * @throws JSONException If a JSON file returned by server is in wrong format
     */
    public String drawWeeklyChart(String dateSince, String dateTo, String currency,String baseChar) throws ParseException, JSONException {

        String monthDateSince = dateSince.substring(0,dateSince.lastIndexOf("-"))+"-01";
        int weekNumberSince = Integer.parseInt(dateSince.substring(dateSince.lastIndexOf("-")+1,dateSince.length()));
        String monthDateTo = dateTo.substring(0,dateTo.lastIndexOf("-"))+"-01";
        int weekNumberTo = Integer.parseInt(dateTo.substring(dateTo.lastIndexOf("-")+1,dateTo.length()));
        Calendar dateSinceParsed = parseDateString(monthDateSince);
        dateSinceParsed.set(Calendar.WEEK_OF_MONTH,weekNumberSince);
        Calendar dateToParsed = parseDateString(monthDateTo);
        dateToParsed.set(Calendar.WEEK_OF_MONTH,weekNumberTo+1);

        StringBuilder sb = new StringBuilder();

        StringBuilder [] days = new StringBuilder[workingDays];

        for(int i=0;i<workingDays;i++){
            days[i] = new StringBuilder();
        }

//        dateSinceParsed.add(Calendar.DATE,3);
//        dateToParsed.add(Calendar.DATE,4);

        boolean flag = true;

        while(!dateSinceParsed.equals(dateToParsed) && flag){
            ICurrency icurrency = null;
            Calendar dateSinceAdded = Calendar.getInstance();
            dateSinceAdded.setTime(dateSinceParsed.getTime());
            dateSinceAdded.add(Calendar.DATE,7);

            //in case we will find a date in which we dont have any data.

            try{
                if(dateSinceAdded.after(dateToParsed)|| dateSinceAdded.equals(dateToParsed)){
                    icurrency = loadCurrencyData(currency,format.format(dateSinceParsed.getTime())+"/"+format.format(dateToParsed.getTime()),"A","rates");
                    flag = false;
                }
                else{
                    icurrency = loadCurrencyData(currency,
                            format.format(dateSinceParsed.getTime())+"/"+format.format(dateSinceAdded.getTime()),"A","rates");
                }
            }
            catch( IOException e){
//                Calendar fin;
//                if(flag) fin = dateSinceAdded;
//                else fin = dateToParsed;
//                for(Calendar i = dateSinceParsed;i.before(fin);i.add(Calendar.DATE,1)){
//                    days[i.get(Calendar.DAY_OF_WEEK)-1].append(format.format(i.getTime())+" No data");
//                }
                //If you want to have information about dates that doesnt have any data untag this^
            }
            AllCurrencyTypeA currencyTypeA = (AllCurrencyTypeA) icurrency;
            dateSinceParsed = dateSinceAdded;
            dateSinceParsed.add(Calendar.DATE,1);
            if(currencyTypeA != null){
                for(CurrencyObject i : currencyTypeA.currencyData){
                    Calendar date = parseDateString(i.getDate());
                    days[date.get(Calendar.DAY_OF_WEEK)-1].append(i.getDate()+" "+stringDays[date.get(Calendar.DAY_OF_WEEK)]+" "+makeBarChart(i.getValueAsk(),0.1,baseChar)+"\n");
                }
            }
        }

        for(int i = 0;i<workingDays;i++){
            sb.append(days[i]);
        }

        return sb.toString();
    }

    /**
     * Analogically to a drawWeeklyChart method it draws a graph but in graphical form.
     * It uses javaFX and parameters are passed via scanner. Also it is a date since, date to and currency symbol.
     */
    public void drawFXChart() {
        launch(new String[0]);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ChartManager cm = new ChartManager();
        Scene scene=cm.getScene();
        stage.setTitle("Bar Chart");
        stage.setScene(scene);
        stage.show();
    }



    //SourceCode.Gold Part:

    public Gold loadDayGoldData(String day) throws IOException, JSONException {
        Gold gold = new Gold(day);
        gold.loadGoldRates(getAllData("http://api.nbp.pl/api/cenyzlota/"+day+"/?format=json"));
        return gold;
    }

    /**
     * Get gold value in specific day
     * @param day
     * @return String with gold value
     * @throws Exception
     */
    public String gatherGoldData(String day) throws Exception {
        Gold object =  this.loadDayGoldData(day);
        return object.toString();
    }

    /**
     * Returns average gold price per 1g in given period. Period can be practically unlimited, but NBP api only contains information
     * about values since 2nd of january 2013.
     * @param dateSince Date since you want to have information in format YYYY-MM-DD
     * @param dateTo Date to you want to have information in format YYYY-MM-DD
     * @return String with average gold price
     * @throws ParseException Thrown if a date is different format that YYYY-MM-DD
     * @throws IOException Thrown if server returns 404
     * @throws JSONException If a JSON file returned by server is in wrong format
     */
    public String gatherAverageGoldPrice(String dateSince, String dateTo) throws IOException, JSONException, ParseException {
        if(dateSince.substring(0,7).equals(dateTo.substring(0,7))){
            Gold gold = loadDayGoldData(dateSince+"/"+dateTo);
            return "Avg gold price in period: " + dateSince +" to " + dateTo + " is "+gold.getAvgFromPeriod();
        }
        else{
            BigDecimal sum = new BigDecimal(0,new MathContext(2));
            BigDecimal days = new BigDecimal(0);
            Calendar dateSinceParsed = parseDateString(dateSince);
            Calendar dateToParsed = parseDateString(dateTo);

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
