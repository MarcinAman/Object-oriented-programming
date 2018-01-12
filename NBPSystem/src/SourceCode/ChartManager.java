package SourceCode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Calendar;
import java.util.spi.CalendarNameProvider;

//https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/bar-chart.htm#CIHJFHDE

public class ChartManager extends Application{

    private DrivingClass dr;
    private static String dateSince = "2017-10-01";
    private static String dateTo = "2017-12-03";
    private static String currency = "usd";

    public void setData(String dateSince, String dateTo,String currency){
        this.dr = new DrivingClass();
        this.dateSince = dateSince;
        this.dateTo = dateTo;
        this.currency = currency;
    }

    public void getChart(){
        launch(new String[0]);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.dr = new DrivingClass();

        stage.setTitle("Bar Chart");
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

        boolean flag = true;

        ICurrency icurrency = null;

        while(!dateSinceParsed.equals(dateToParsed) && flag){
            Calendar dateSinceAdded = Calendar.getInstance();
            dateSinceAdded.setTime(dateSinceParsed.getTime());
            dateSinceAdded.add(Calendar.DATE,10);

            //in case we will find a date in which we dont have any data.

            try
            {
                if(dateSinceAdded.after(dateToParsed)|| dateSinceAdded.equals(dateToParsed)){
                    icurrency = dr.loadCurrencyData(currency,dr.format.format(dateSinceParsed.getTime())+"/"+
                            dr.format.format(dateToParsed.getTime()),"A","rates");;
                    flag = false;
                }
                else{
                    icurrency = dr.loadCurrencyData(currency,dr.format.format(dateSinceParsed.getTime())+"/"+
                            dr.format.format(dateSinceAdded.getTime()),"A","rates");
                }
            }
            catch(IOException e){
                dateSinceAdded.add(Calendar.DATE,1);
            }
            AllCurrencyTypeA currencyTypeA = (AllCurrencyTypeA) icurrency;
            dateSinceParsed = dateSinceAdded;

            for(CurrencyObject i : currencyTypeA.currencyData){
                XYChart.Series series = new XYChart.Series();
                series.setName(i.getDate());
                series.getData().add(new XYChart.Data(i.getDate(),i.getGenericValueAsk()));
                bc.getData().add(series);
            }
        }
        stage.setScene(scene);
        stage.show();
    }
}
