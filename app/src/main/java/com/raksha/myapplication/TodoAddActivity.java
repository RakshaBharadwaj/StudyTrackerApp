package com.raksha.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class TodoAddActivity extends AppCompatActivity  {
    // TextInputLayout test_id,sub_id,marks;
    TextInputLayout marks_marks;
    String dbdate;
    Button dates;
    Button create_marks,cancel_marks;
    String display_date;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    SQLiteDatabase mydb;

    int year;
    int month ;
    int day;

    String databasename = "mydatabase.db";
    String tablename = "todo_master";





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_add);

        GetReadyWithDatabase();



        marks_marks =(TextInputLayout)findViewById(R.id.edt_task);

        dates=(Button)findViewById(R.id.date_selector_task);
        create_marks =findViewById(R.id.btn_create_task);
        cancel_marks =findViewById(R.id.btn_cancel_task);

        dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        TodoAddActivity.this,
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

                addRecord();

            }


        });
        cancel_marks.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                startActivity(new Intent(TodoAddActivity.this, TodoActivity.class));


            }


        });




    }




    public void GetReadyWithDatabase() {


        mydb = this.openOrCreateDatabase(databasename, MODE_PRIVATE, null);

        String stg = "CREATE TABLE IF NOT EXISTS ";
        stg = stg + tablename + " (";
        stg = stg + "todokey integer primary key autoincrement,";

        stg = stg + "todo varchar(30),";

        stg = stg + "flddate varchar(30),";
        stg = stg + "displaydate varchar(30));";



        mydb.execSQL(stg);









    }

    public void onBackPressed() {
        startActivity(new Intent(TodoAddActivity.this, TodoActivity.class));
        finish();
    }

    private void addRecord() {

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


        String todo="todo";

        String flddate="flddate";
        String displaydate="displaydate";



        String todoval = marks_marks.getEditText().getText().toString().trim();
        String dateval= dbdate ;
        String displaydateval = display_date;



        String stg;
        stg = "INSERT INTO  " + tablename  + "(" ;

        stg = stg  + todo + ",";
        stg = stg  + flddate + ",";
        stg = stg  + displaydate + ")";


        stg = stg + "  VALUES (" ;
        stg = stg +"'"+ todoval + "',";
        stg = stg +"'"+  dateval  + "',";
        stg = stg +"'"+  displaydateval  + "');";


        mydb.execSQL(stg);

        Toast.makeText(TodoAddActivity.this,"DONE",Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(TodoAddActivity.this, TodoActivity.class);
        startActivity(intent);

    }



}
