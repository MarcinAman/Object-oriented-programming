package SourceCode;

import org.json.JSONException;

import java.io.IOException;


public class MainNBPClass{


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
            if(args.length == 0 || args[0].equals("--help")){
                displayHelp();
            }
            else{
                if(args[0].equals("dayCurrency") && args.length>0){
                    System.out.println(drivingClass.gatherDayCurrencyDataTableA(args[1],args[2]));
                }
                else if(args[0].equals("dayGold") && args.length==2){
                    System.out.println(drivingClass.gatherGoldData(args[1]));
                }
                else if(args[0].equals("avgGold") && args.length == 3){
                    System.out.println(drivingClass.gatherAverageGoldPrice(args[1],args[2]));
                }
                else if(args[0].equals("minCurrency")&& args.length == 2){
                    System.out.println(drivingClass.getMinAskCurrency(args[1]));
                }
                else if(args[0].equals("maxDiffCurrency") && args.length == 3){
                    System.out.println(drivingClass.getMaxDiffrenceCurrencies(args[1],Integer.parseInt(args[2])));
                }
                else if(args[0].equals("maxMinOf") && args.length == 4){
                    System.out.println(drivingClass.getMaxMinOfCurrency(args[1],args[2],args[3]));
                }
                else if(args[0].equals("maxAmplitudeCur") && args.length == 3){
                    System.out.println(drivingClass.getMaxAplitudeCurrency(args[1],args[2]));
                }
                else if(args[0].equals("drawWeeklyChart") && args.length == 5){
                    System.out.print(drivingClass.drawWeeklyChart(args[1],args[2],args[3],args[4]));
                }
                else if(args[0].equals("drawFXChart") && args.length == 0){
                    drivingClass.drawFXChart();
                }
                else{
                    displayHelp();
                }
            }
//            System.out.println(drivingClass.gatherDayCurrencyDataTableA("GBP","2015-12-15"));
//            System.out.println(drivingClass.gatherDayCurrencyDataTableA("USD","2016-05-04"));
//            System.out.println(drivingClass.gatherDayCurrencyDataTableA("USD","2017-04-04"));
//            System.out.println(drivingClass.gatherGoldData("2017-12-15"));
//            System.out.println(drivingClass.gatherAverageGoldPrice("2017-11-15","2017-11-20"));
//            System.out.println(drivingClass.getMinAskCurrency("2017-11-21"));
//            System.out.println(drivingClass.getMaxDiffrenceCurrencies("2017-12-21",10));
//            System.out.println(drivingClass.getMaxMinOfCurrency("GBP","2013-12-15","2017-11-20"));
//            System.out.println(drivingClass.getMaxAplitudeCurrency("2013-01-21","2017-12-23"));
//            System.out.println(drivingClass.drawWeeklyChart("2017-12-1","2018-01-2","usd","\u25A1"));
//           drivingClass.drawFXChart();

            //
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
