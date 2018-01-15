package SourceCode;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Loader {
    /**
     * Method is responsible for sending requests to a api server.
     * String value of the api response is being returned
     * after that connection to a server is closed
     *
     * @param URLs url connection
     * @return String value of api response
     * @throws IOException Thrown if server response is "404"
     */

    public static String getAllData(String URLs) throws IOException {
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
    public static ICurrency loadCurrencyData(String name, String date,String table,String column) throws IOException, JSONException {
        ICurrency currency = null;
        if(table.equals("A")||table.startsWith("A")){
            currency =  new AllCurrencyTypeA(date);
        }
        else{
            currency =  new AllCurrencyTypeC(date);
        }
        if(name==null){
            currency.getAllCurrency(Loader.getAllData("http://api.nbp.pl/api/exchangerates/"+column+"/"+table+"/"+date+"/?format=json"));
        }
        else{
            currency.getAllCurrency(Loader.getAllData("http://api.nbp.pl/api/exchangerates/"+column+"/"+table+"/"+name+"/"+date+"/?format=json"));

        }
        return currency;
    }

    public static ICurrency loadDayCurrencyDataTableC(String day) throws IOException, JSONException {
        ICurrency currency = loadCurrencyData(null,day,"C","tables");
        return currency;
    }

    public static Gold loadDayGoldData(String day) throws IOException, JSONException {
        Gold gold = new Gold(day);
        gold.loadGoldRates(Loader.getAllData("http://api.nbp.pl/api/cenyzlota/"+day+"/?format=json"));
        return gold;
    }
}
