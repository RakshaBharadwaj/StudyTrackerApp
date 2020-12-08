package com.raksha.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;

import static java.lang.System.exit;

public class MarksEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    // TextInputLayout test_id,sub_id,marks;
    TextInputLayout marks_marks;
    String dbdate;
    Button dates;

    int year;
    int month ;
    int day;
    Button create_marks,cancel_marks;
    String test,sub,display_date;
    String markskey,testkey,subkey,testname,subname,flddate,displaydate,marks;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    ArrayList<String> test_id_marks,test_name_marks,max_marks_marks;
    ArrayList<String> sub_id_marks, sub_name_marks, max_credits_marks;
    SQLiteDatabase mydb;

    String databasename = "mydatabase.db";
    String tablename = "mark_master";
    String tablename2="testmaster";
    String tablename3="submaster";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks_add);

        GetReadyWithDatabase();
        mydb = this.openOrCreateDatabase(databasename, MODE_PRIVATE, null);

        String stgk = "CREATE TABLE IF NOT EXISTS ";
        stgk = stgk+ tablename2 + " (";
        stgk = stgk + "testkey integer primary key autoincrement,";
        stgk= stgk + "testname varchar(25),";
        stgk = stgk + "maxmarks integer);";

        mydb = this.openOrCreateDatabase(databasename, MODE_PRIVATE, null);

        mydb.execSQL(stgk);
        String stgs = "CREATE TABLE IF NOT EXISTS ";
        stgs = stgs+ tablename2 + " (";
        stgs = stgs + "subkey integer primary key autoincrement,";
        stgs = stgs + "subname varchar(25),";
        stgs = stgs + "maxcredits integer);";


        mydb.execSQL(stgs);

        marks_marks =(TextInputLayout)findViewById(R.id.edt_marks_marks);

        dates=(Button)findViewById(R.id.date_selector);
        create_marks =findViewById(R.id.btn_create_marks);
        cancel_marks =findViewById(R.id.btn_cancel_marks);



        test_id_marks =new ArrayList<>();
        test_name_marks =new ArrayList<>();
        max_marks_marks =new ArrayList<>();

        sub_id_marks = new ArrayList<>();
        sub_name_marks = new ArrayList<>();
        max_credits_marks = new ArrayList<>();



        Cursor ck;
        String stgd = "SELECT * FROM " + tablename3;
        ck = mydb.rawQuery(stgd, null);
        ck.moveToFirst();
        if (ck.getCount() == 0) {
            Toast.makeText(MarksEditActivity.this, "no tests yet", Toast.LENGTH_SHORT).show();
        } else {
            do {
                sub_id_marks.add(ck.getString(0));
                sub_name_marks.add(ck.getString(1));
                max_credits_marks.add(ck.getString(2));

            } while (ck.moveToNext());
        }



        Cursor c;
        String stg  ="SELECT * FROM " + tablename2 ;
        c  = mydb.rawQuery(stg , null);
        c.moveToFirst();
        if(c.getCount()==0){
            Toast.makeText(MarksEditActivity.this,"no tests yet",Toast.LENGTH_SHORT).show();
        }else{
            do{
                test_id_marks.add(c.getString(0));
                test_name_marks.add(c.getString(1));
                max_marks_marks.add(c.getString(2));

            }while(c.moveToNext());
        }

        markskey = getIntent().getExtras().getString("markskey");
        testkey = getIntent().getExtras().getString("testkey");
        subkey = getIntent().getExtras().getString("subkey");
        testname = getIntent().getExtras().getString("testname");
         subname = getIntent().getExtras().getString("subname");
        flddate = getIntent().getExtras().getString("flddate");
         displaydate =getIntent().getExtras().getString("displaydate");
         marks = getIntent().getExtras().getString("marks");

       /* Toast.makeText(MarksEditActivity.this, "markskey " + markskey, Toast.LENGTH_SHORT).show();
        Toast.makeText(MarksEditActivity.this, "testkey " + testkey, Toast.LENGTH_SHORT).show();
        Toast.makeText(MarksEditActivity.this, "subkey " + subkey, Toast.LENGTH_SHORT).show();
        Toast.makeText(MarksEditActivity.this, "testname " + testname, Toast.LENGTH_SHORT).show();
        Toast.makeText(MarksEditActivity.this, "subname " + subname,Toast.LENGTH_SHORT).show();
        Toast.makeText(MarksEditActivity.this, "flddate " + flddate,Toast.LENGTH_SHORT).show();
        Toast.makeText(MarksEditActivity.this, "displaydate " + displaydate, Toast.LENGTH_SHORT).show();
        Toast.makeText(MarksEditActivity.this, "marks " + marks, Toast.LENGTH_SHORT).show();*/

        marks_marks.getEditText().setText(marks);
        Spinner spinner_test = findViewById(R.id.spinner_test);
        Spinner spinner_sub =findViewById(R.id.spinner_sub);

         final String mdate[] =displaydate.split("/");



        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, test_name_marks);
        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sub_name_marks);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_test.setAdapter(adapter);
        spinner_test.setSelection(adapter.getPosition(testname));

        spinner_test.setOnItemSelectedListener(this);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sub.setAdapter(adapter2);
        spinner_sub.setSelection(adapter2.getPosition(subname));
        spinner_sub.setOnItemSelectedListener(this);

        dates.setText(displaydate);
        dbdate= flddate;
        display_date = displaydate;
        dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Calendar cal = Calendar.getInstance();

                year = Integer.valueOf(mdate[2]);
                month =Integer.valueOf(mdate[1]);
                day = Integer.valueOf(mdate[0]);

                DatePickerDialog dialog = new DatePickerDialog(
                        MarksEditActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String setday=String.valueOf(day);
                String setmonth="";

                if (day >= 1 && day<= 9) {
                    setday = "0" + day;
                }


                if (month >= 1 && month <= 9) {
                    setmonth = "0" + month;
                }
                if (setday.length() == 1)
                    setday = "0" + setday;
                if (setmonth.length() == 1)
                    setmonth = "0" + setmonth;

                dbdate = year + "-" + setmonth + "-" + setday + " 10:10:10 AM";
                display_date = day + "/" + month + "/" + year;
                dates.setText(display_date);
            }
        };

        create_marks.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                editRecord(markskey);

            }


        });

        cancel_marks.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                startActivity(new Intent(MarksEditActivity.this, MarksActivity.class));


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
        startActivity(new Intent(MarksEditActivity.this, MarksActivity.class));
        finish();
    }

    private void editRecord(String id) {


        //String stg_test_name = test_title.getEditText().getText().toString().trim();
        String stg_marks_marks = marks_marks.getEditText().getText().toString().trim();




       /* if (stg_test_name.isEmpty()) {
            test_title.setError("Test Name Required");
            test_title.requestFocus();
            return;

        } else {
            test_title.setErrorEnabled(false);
        }



        if (stg_max_marks.isEmpty()) {
            max_marks.setError("Maximum Marks Required");
            max_marks.requestFocus();

            return;

        } else {
            max_marks.setErrorEnabled(false);
        }
        if (stg_max_marks.length()>3) {
            max_marks.setError("Enter valid marks");
            max_marks.requestFocus();
            return;
        } else {
            max_marks.setErrorEnabled(false);
        }*/

        String markskey="markskey";
        String testkey="testkey";
        String subkey="subkey";
        String testname="testname";
        String subname="subname";
        String flddate="flddate";
        String displaydate="displaydate";
        String marks ="marks";

        int testidval=2;
        int subidval=30;
        String testnameval="";
        String subnameval="";
        String dateval= dbdate ;
        String marksval= stg_marks_marks;
        String displaydateval = display_date;
        testnameval = String.valueOf(test) ;
        subnameval =String.valueOf(sub);

        Cursor cs;
        String stgs = "SELECT * FROM " + tablename2 + " WHERE testname='" + test + "'";
        cs  = mydb.rawQuery(stgs, null);
        cs.moveToLast();

        int Column1 = cs.getColumnIndex("testkey");
        cs.moveToFirst();

        int countrecords = cs.getCount();
        if(countrecords==0)
        {
            Toast.makeText(MarksEditActivity.this, "No. of Entries  : " + countrecords, Toast.LENGTH_LONG).show();

            finish();
        }
        for (int i=1;i<=countrecords;i++)
        {
            testidval = Integer.parseInt(cs.getString(Column1));
            cs.moveToNext();
        }
        cs.close();


        Cursor csk;
        String stgsk = "SELECT * FROM " + tablename3 + " WHERE subname ='"+sub+"'";
        csk  = mydb.rawQuery(stgsk, null);
        csk.moveToLast();

        int Column2 = csk.getColumnIndex("subkey");
        csk.moveToFirst();

        int countrecords2 = csk.getCount();
        if(countrecords2==0)
        {
            Toast.makeText(MarksEditActivity.this, "No. of Entries  : " + countrecords2, Toast.LENGTH_LONG).show();

            finish();
        }
        for (int i=1;i<=countrecords2;i++)
        {
            subidval = Integer.parseInt(csk.getString(Column2));
            csk.moveToNext();
        }
        csk.close();

    /*
        String stg;
        stg = "INSERT INTO  " + tablename  + "(" ;

        stg = stg  + testkey + ",";
        stg = stg  + subkey + ",";
        stg = stg  + testname + ",";
        stg = stg  + subname + ",";
        stg = stg  + flddate + ",";
        stg = stg  + displaydate + ",";
        stg = stg + marks +  ")";

        stg = stg + "  VALUES (" ;
        stg = stg +  testidval  + ",";
        stg = stg +  subidval  + ",";
        stg = stg +"'"+  testnameval  + "',";
        stg = stg +"'"+  subnameval  + "',";
        stg = stg +"'"+  dateval  + "',";
        stg = stg +"'"+  displaydateval  + "',";
        stg = stg +  marksval + ");";

        mydb.execSQL(stg);*/


        Integer myfldkey = Integer.parseInt(id);

        ContentValues cv = new ContentValues();

        cv.put("testkey",testidval );
        cv.put("subkey",subidval);
        cv.put("testname",testnameval);
        cv.put("subname",subnameval);
        cv.put("flddate",dateval);
        cv.put("displaydate",displaydateval);
        cv.put("marks",marksval);

        String condition= " markskey=" + myfldkey;
        mydb.update(tablename, cv, condition, null);

        Toast.makeText(MarksEditActivity.this,"Sucessfully Updated",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MarksEditActivity.this, MarksActivity.class);
        startActivity(intent);



    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;

        if (spinner.getId() == R.id.spinner_test) {

            test = parent.getItemAtPosition(position).toString();


        } else if (spinner.getId() == R.id.spinner_sub) {

            sub = parent.getItemAtPosition(position).toString();



        }

    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}