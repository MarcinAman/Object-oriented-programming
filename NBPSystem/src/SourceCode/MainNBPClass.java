package SourceCode;

import org.json.JSONException;

import java.io.IOException;

public class MainNBPClass {

    public static void displayHelp(){
        System.out.println("Help: \n" +
                "--dayCurrency <CurrencySymbol> <Date> \n" +
                "--dayGold <Date> \n" +
                "--avgGold <DateSince> <DateTo> \n" +
                "--minCurrency <Date> \n" +
                "--maxDiffCurrency <Date> <Amount> \n" +
                "--maxMinOf <CurrencySymbol> <DateSince> <DateTo> \n" +
                "--maxAmplitudeCur <DateSince> <DateTo> \n" +
                "--drawWeeklyChart <CurrencySymbol> <DateSince> <DateTo> <char> \n" +
                "Date format is always yyyy-mm-dd or yyyy-mm-<week number> if you are using the --drawWeeklyChar \n");
    }

    public static void main(String [] args){
       DrivingClass drivingClass = new DrivingClass();
        try {
//            if(args.length == 0 || args[0].equals("--help")){
//                displayHelp();
//            }
//            else{
//                if(args[0].equals("dayCurrency")){
//                    System.out.println(drivingClass.gatherDayCurrencyDataTableA(args[1],args[2]));
//                }
//                else if(args[0].equals("dayGold")){
//                    System.out.println(drivingClass.gatherGoldData(args[1]));
//                }
//                else if(args[0].equals("avgGold")){
//                    System.out.println(drivingClass.gatherAverageGoldPrice(args[1],args[2]));
//                }
//                else if(args[0].equals("minCurrency")){
//                    System.out.println(drivingClass.getMinAskCurrency(args[1]));
//                }
//                else if(args[0].equals("maxDiffCurrency")){
//                    System.out.println(drivingClass.getMaxDiffrenceCurrencies(args[1],Integer.parseInt(args[2])));
//                }
//                else if(args[0].equals("maxMinOf")){
//                    System.out.println(drivingClass.getMaxMinOfCurrency(args[1],args[2],args[3]));
//                }
//                else if(args[0].equals("maxAmplitudeCur")){
//                    System.out.println(drivingClass.getMaxAplitudeCurrency(args[1],args[2]));
//                }
//                else if(args[0].equals("drawWeeklyChart")){
//                    System.out.print(drivingClass.drawWeeklyChart(args[1],args[2],args[3],args[4].charAt(0)));
//                }else{
//                    displayHelp();
//                }
//            }
            System.out.println(drivingClass.gatherDayCurrencyDataTableA("GBP","2015-12-15"));
            System.out.println(drivingClass.gatherGoldData("2017-12-15"));
            System.out.println(drivingClass.gatherAverageGoldPrice("2013-01-01","2017-11-20"));
            System.out.println(drivingClass.getMinAskCurrency("2017-12-21"));
            System.out.println(drivingClass.getMaxDiffrenceCurrencies("2017-12-21",10));
            System.out.println(drivingClass.getMaxMinOfCurrency("GBP","2013-12-15","2017-11-20"));
            System.out.println(drivingClass.getMaxAplitudeCurrency("2013-01-21","2017-12-23"));
            System.out.print(drivingClass.drawWeeklyChart("2017-10-1","2017-12-3","usd",'+'));
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
