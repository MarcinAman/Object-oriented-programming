import org.json.JSONException;

import java.io.IOException;

public class MainNBPClass {

    public static void main(String [] args){
       DrivingClass drivingClass = new DrivingClass(null);
        try {
//            System.out.println(drivingClass.gatherDayCurrencyDataTableA("GBP","2015-12-15"));
//            System.out.println(drivingClass.gatherGoldData("2017-12-15"));
//            System.out.println(drivingClass.gatherAverageGoldPrice("2013-01-01","2017-11-20"));
//            System.out.println(drivingClass.getMinAskCurrency("2017-12-21"));
//            System.out.println(drivingClass.getMaxDiffrenceCurrencies("2017-12-21",10));
//            System.out.println(drivingClass.getMaxMinOfCurrency("GBP","2013-12-15","2017-11-20"));
            System.out.println(drivingClass.getMaxAplitudeCurrency("2017-01-21","2017-12-23"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//http://www.jfree.org/jfreechart/download.html
//https://stackoverflow.com/questions/16714738/xy-plotting-with-java
