package com.raksha.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.textfield.TextInputLayout;

import java.text.NumberFormat;
import java.util.ArrayList;


public class ChartActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button generate;
    BarChart barChart;
    String test,sub;
    LinearLayout chartHome;
    ArrayList<BarEntry> barEntriesArrayList;
    ArrayList<String > lableName;
    ArrayList<MarksData> marksDataArrayList = new ArrayList<>();
    AlertDialog alertDialog;
    String databasename = "mydatabase.db";
    String tablename = "mark_master";
    String tablename2="testmaster";
    String tablename3="submaster";

    ArrayList<String> test_id_marks,test_name_marks,max_marks_marks;
    ArrayList<String> sub_id_marks, sub_name_marks, max_credits_marks;

    SQLiteDatabase mydb;



    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        generate =findViewById(R.id.btn_generate_charts);
        barChart = findViewById(R.id.barChart);
        chartHome= findViewById(R.id.chart_home);
        barEntriesArrayList = new ArrayList<>();
        lableName  = new ArrayList<>();
        barChart.setNoDataText("Your Chart Will Appear Here !");
        Paint p = barChart.getPaint(barChart.PAINT_INFO);
        p.setTextSize(50);

        chartHome.setVisibility(View.INVISIBLE);
        p.setColor(getResources().getColor(R.color.pinktrend));

        GetReadyWithDatabase();


        mydb = this.openOrCreateDatabase(databasename, MODE_PRIVATE, null);

        String stgk = "CREATE TABLE IF NOT EXISTS ";
        stgk = stgk+ tablename2 + " (";
        stgk = stgk + "testkey integer primary key autoincrement,";
        stgk= stgk + "testname varchar(25),";
        stgk = stgk + "maxmarks integer);";



        mydb.execSQL(stgk);
        String stgs = "CREATE TABLE IF NOT EXISTS ";
        stgs = stgs+ tablename3 + " (";
        stgs = stgs + "subkey integer primary key autoincrement,";
        stgs = stgs + "subname varchar(25),";
        stgs = stgs + "maxcredits integer);";


        mydb.execSQL(stgs);



        Cursor ck;
        String stgi  ="SELECT * FROM " + tablename2 ;
        ck  = mydb.rawQuery(stgi , null);
        ck.moveToFirst();
        int count_test=ck.getCount();


        if(count_test==0) {


            String testname = "testname";
            String maxmarks = "maxmarks";
            String testnameval = "SEE";
            int maxmarksval = 100;

            String stge;
            stge = "INSERT INTO  " + tablename2 + "(";

            stge = stge + testname + ",";
            stge = stge + maxmarks + ")";

            stge = stge + "  VALUES (";
            stge = stge + "'" + testnameval + "',";

            stge = stge + maxmarksval + ");";

            mydb.execSQL(stge);
            testnameval="CIE";
            maxmarksval=20;

            String stq;
            stq= "INSERT INTO  " + tablename2  + "(" ;

            stq = stq  + testname + ",";
            stq = stq + maxmarks +  ")";

            stq = stq + "  VALUES (" ;
            stq = stq +"'"+  testnameval  + "',";

            stq = stq +  maxmarksval + ");";

            mydb.execSQL(stq);
        }

        Cursor cs;
        String stgsi  ="SELECT * FROM " + tablename3 ;
        cs  = mydb.rawQuery(stgsi, null);
        cs.moveToFirst();
        int count_subject=cs.getCount();

        if(count_subject==0){
            String subname="subname";
            String maxcredits="maxcredits";
            String subnameval="Subject";
            int maxcreditsval=4;

            String stgd;
            stgd = "INSERT INTO  " + tablename3  + "(" ;

            stgd = stgd  + subname + ",";
            stgd = stgd+ maxcredits +  ")";

            stgd = stgd + "  VALUES (" ;
            stgd = stgd +"'"+  subnameval  + "',";

            stgd = stgd +  maxcreditsval + ");";

            mydb.execSQL(stgd);

        }

        Cursor c;
        String stg  ="SELECT * FROM " + tablename ;
        c  = mydb.rawQuery(stg , null);
        c.moveToFirst();
        int count=c.getCount();
        if(count==0)
        {
            Toast.makeText(ChartActivity.this, "No Entries Found", Toast.LENGTH_LONG).show();
           
        }



        test_id_marks =new ArrayList<>();
        test_name_marks =new ArrayList<>();
        max_marks_marks =new ArrayList<>();

        sub_id_marks = new ArrayList<>();
        sub_name_marks = new ArrayList<>();
        max_credits_marks = new ArrayList<>();



        Cursor ckd;
        String stgd = "SELECT * FROM " + tablename3;
        ckd = mydb.rawQuery(stgd, null);
        ckd.moveToFirst();
        if (ckd.getCount() == 0) {
            Toast.makeText(ChartActivity.this, "no tests yet", Toast.LENGTH_SHORT).show();
        } else {
            do {
                sub_id_marks.add(ckd.getString(0));
                sub_name_marks.add(ckd.getString(1));
                max_credits_marks.add(ckd.getString(2));

            } while (ckd.moveToNext());
        }



        Cursor cd;
        String stgdu  ="SELECT * FROM " + tablename2 ;
        cd  = mydb.rawQuery(stgdu , null);
        cd.moveToFirst();
        if(cd.getCount()==0){
            Toast.makeText(ChartActivity.this,"no tests yet",Toast.LENGTH_SHORT).show();
        }else{
            do{
                test_id_marks.add(cd.getString(0));
                test_name_marks.add(cd.getString(1));
                max_marks_marks.add(cd.getString(2));

            }while(cd.moveToNext());
        }

        Spinner spinner_test = findViewById(R.id.spinner_test_chart);
        Spinner spinner_sub =findViewById(R.id.spinner_sub_chart);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, test_name_marks);
        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sub_name_marks);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_test.setAdapter(adapter);
        spinner_test.setOnItemSelectedListener(this);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sub.setAdapter(adapter2);
        spinner_sub.setOnItemSelectedListener(this);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String stg = "SELECT * FROM " + tablename +" WHERE testname='"+test+"' AND subname='"+ sub+"'" ;
                Cursor c = mydb.rawQuery(stg, null);
                c.moveToFirst();

                int countrecords = c.getCount();

                if(countrecords!=0) {

                    marksDataArrayList.clear();
                    fillMonthlySalesArrayList();

                    for (int i = 0; i < marksDataArrayList.size(); i++) {

                        String date = marksDataArrayList.get(i).getDate();
                        int marks = marksDataArrayList.get(i).getMarks();
                        barEntriesArrayList.add(new BarEntry(i, marks));
                        lableName.add(date);
                    }


                    BarDataSet barDataSet = new BarDataSet(barEntriesArrayList, "Marks Obtained");
                    barDataSet.setColors(getResources().getColor(R.color.pinktrend));
                    Description description = new Description();
                    description.setText("Date");
                    barChart.setDescription(description);
                    chartHome.setVisibility(View.VISIBLE);
                    BarData barData = new BarData(barDataSet);
                    barChart.setData(barData);
                    barChart.fitScreen();
                    barChart.getLegend().setWordWrapEnabled(true);
                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(lableName));
                    xAxis.setTextSize(8);

                    xAxis.setGridColor(getResources().getColor(R.color.pinktrend));
                    xAxis.setDrawGridLines(true);
                    xAxis.setDrawAxisLine(false);
                    xAxis.setGranularity(1f);
                    xAxis.setLabelCount(lableName.size());
                    xAxis.setLabelRotationAngle(0);
                    xAxis.setAxisLineColor(getResources().getColor(R.color.Blue));
                    YAxis rightYAxis = barChart.getAxisRight();
                    rightYAxis.setEnabled(false);
                    rightYAxis.setGridColor(getResources().getColor(R.color.pinktrend));

                    rightYAxis.setDrawAxisLine(false);
                    //barChart.setVisibleYRangeMaximum(100, XAxis.AxisDependency.RIGHT);
                    barChart.animateY(2020);
                    barChart.invalidate();
                    generate.setClickable(false);

                    //barChart.setExtraTopOffset(10);
                }
                else{
                    Toast.makeText(ChartActivity.this,"No Entries Found",Toast.LENGTH_SHORT).show();
                    Intent i =new Intent(ChartActivity.this,ChartActivity.class);
                    startActivity(i);
                }
            }
        });

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int x = barChart.getData().getDataSetForEntry(e).getEntryIndex((BarEntry) e);
                String region = marksDataArrayList.get(x).getDate();
                String sales =  String.valueOf(marksDataArrayList.get(x).getMarks());
                AlertDialog.Builder builder = new AlertDialog.Builder(ChartActivity.this);
                builder.setCancelable(true);
                View view = LayoutInflater.from(ChartActivity.this).inflate(R.layout.chart_popup,null);
                TextView regionTxtView = view.findViewById(R.id.txt_popup1);
                TextView salesTxtView = view.findViewById(R.id.txt_popup2);
                regionTxtView.setText("Date: "+region);
                salesTxtView.setText("Marks Obtained: "+sales);
                builder.setView(view);
                alertDialog = builder.create();
                alertDialog.show();

            }

            @Override
            public void onNothingSelected() {

            }
        });



    }

    public void GetReadyWithDatabase() {
        mydb = this.openOrCreateDatabase(databasename, MODE_PRIVATE, null);

        String stg = "CREATE TABLE IF NOT EXISTS ";
        stg = stg + tablename + " (";
        stg = stg + "markskey integer primary key autoincrement,";
        stg= stg  + "testkey integer,";
        stg= stg  + "subkey integer,";
        stg= stg  + "testname varchar(30),";
        stg= stg  + "subname varchar(30),";
        stg= stg  + "flddate varchar(30),";
        stg= stg  + "displaydate varchar(30),";
        stg = stg + "marks integer);";


        mydb.execSQL(stg);

    }

    public void onBackPressed() {
        startActivity(new Intent(ChartActivity.this, MainActivity2.class));
        finish();
    }

    private void fillMonthlySalesArrayList(){

        String stg = "SELECT * FROM " + tablename +" WHERE testname='"+test+"' AND subname='"+ sub+"'" ;
        Cursor c = mydb.rawQuery(stg, null);
        c.moveToLast();

        int Column1 = c.getColumnIndex("markskey");
        int Column2 = c.getColumnIndex("testkey");
        int Column3 = c.getColumnIndex("subkey");
        int Column4 = c.getColumnIndex("testname");
        int Column5 = c.getColumnIndex("subname");
        int Column6 = c.getColumnIndex("flddate");
        int Column7 = c.getColumnIndex("displaydate");
        int Column8 = c.getColumnIndex("marks");
        c.moveToFirst();

        int countrecords = c.getCount();
        if(countrecords==0)
        {
            Toast.makeText(ChartActivity.this, "No Entries Found", Toast.LENGTH_LONG).show();
            Intent i =new Intent(ChartActivity.this,ChartActivity.class);
            startActivity(i);
        }
        String markskey="";
        String testkey="";
        String subkey="";
        String testname="";
        String subname="";
        String flddate="";
        String displaydate="";
        String marks="";
        marksDataArrayList.clear();
        for (int i=1;i<=countrecords;i++)
        {
            markskey =  c.getString(Column1);
            testkey = c.getString(Column2);
            subkey = c.getString(Column3);
            testname = c.getString(Column4);
            subname = c.getString(Column5);
            flddate = c.getString(Column6);
            displaydate =c.getString(Column7);
            marks = c.getString(Column8);

            marksDataArrayList.add(new MarksData(displaydate,Integer.valueOf(marks)));
            c.moveToNext();
        }
        c.close();




      /*  marksDataArrayList.add(new MarksData("Feb",300));
        marksDataArrayList.add(new MarksData("Mar",150));
        marksDataArrayList.add(new MarksData("Apr",250));
        marksDataArrayList.add(new MarksData("May",242));
        marksDataArrayList.add(new MarksData("June",300));
        marksDataArrayList.add(new MarksData("July",150));
        marksDataArrayList.add(new MarksData("Aug",210));
        marksDataArrayList.add(new MarksData("Sep",242));
        marksDataArrayList.add(new MarksData("Oct",320));
        marksDataArrayList.add(new MarksData("Nov",150));
        marksDataArrayList.add(new MarksData("Dec",200));*/
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;

        if (spinner.getId() == R.id.spinner_test_chart){

            test = parent.getItemAtPosition(position).toString();
            generate.setClickable(true);
            marksDataArrayList.clear();
            barEntriesArrayList.clear();

        } else if (spinner.getId() == R.id.spinner_sub_chart){

            sub = parent.getItemAtPosition(position).toString();
            generate.setClickable(true);
            marksDataArrayList.clear();
            barEntriesArrayList.clear();


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}

