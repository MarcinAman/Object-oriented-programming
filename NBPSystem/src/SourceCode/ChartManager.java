package SourceCode;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.io.IOException;
import java.util.Calendar;
import java.util.Scanner;

//https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/bar-chart.htm#CIHJFHDE

public class ChartManager{

    private DrivingClass dr;
    private String dateSince;
    private String dateTo;
    private String currency;
    private static int workingDays = 7;


    public void fillData(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Pass a date since in format YYYY-MM-DD");
        this.dateSince = scanner.nextLine().trim();

        System.out.println("Pass a date to in format YYYY-MM-DD");
        this.dateTo = scanner.nextLine().trim();

        System.out.println("And a currency symbol");
        this.currency = scanner.nextLine().trim();
    }

    public Scene getScene() throws Exception {
        this.dr = new DrivingClass();

        fillData();

        NumberAxis xAxis = new NumberAxis();
        CategoryAxis yAxis = new CategoryAxis();
        BarChart<Number,String> bc = new BarChart<>(xAxis,yAxis);
        bc.setTitle("Currency cost summary");
        xAxis.setLabel("Value");
        xAxis.setTickLabelRotation(90);
        yAxis.setLabel("Day");

        Scene scene  = new Scene(bc,800,600);

        Calendar dateSinceParsed = dr.parseDateString(dateSince);
        Calendar dateToParsed = dr.parseDateString(dateTo);

        XYChart.Series [] days = new XYChart.Series[workingDays];

        for(int i=0;i<workingDays;i++){
            days[i] = new XYChart.Series();
            days[i].setName(DrivingClass.stringDays[i+1]);
        }

        boolean flag = true;

        ICurrency icurrency = null;

        while(!dateSinceParsed.equals(dateToParsed) && flag){
            Calendar dateSinceAdded = Calendar.getInstance();
            dateSinceAdded.setTime(dateSinceParsed.getTime());
            dateSinceAdded.add(Calendar.DATE,1);

            //in case we will find a date in which we dont have any data.

            try
            {
                if(dateSinceAdded.after(dateToParsed)|| dateSinceAdded.equals(dateToParsed)){
                    icurrency = Loader.loadCurrencyData(currency,dr.format.format(dateSinceParsed.getTime()),"A","rates");;
                    flag = false;
                }
                else{
                    icurrency = Loader.loadCurrencyData(currency,
                            dr.format.format(dateSinceAdded.getTime()),"A","rates");
                }
            }
            catch(IOException e){
                dateSinceAdded.add(Calendar.DATE,1);
            }
            AllCurrencyTypeA currencyTypeA = (AllCurrencyTypeA) icurrency;
            dateSinceParsed = dateSinceAdded;
            if(currencyTypeA != null){
                for(CurrencyObject i : currencyTypeA.currencyData){
                    Calendar date = dr.parseDateString(i.getDate());
                    days[date.get(Calendar.DAY_OF_WEEK)-1].getData().add(new XYChart.Data(i.getGenericValueAsk(),i.getDate()));
                }
            }
        }
        bc.getData().addAll(days);
        bc.setBarGap(1);
        bc.setCategoryGap(1);
        return scene;
    }
}
