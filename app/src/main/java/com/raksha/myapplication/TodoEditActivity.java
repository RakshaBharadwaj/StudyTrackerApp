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

public class TodoEditActivity extends AppCompatActivity  {
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
    String todokey,flddate,displaydate,todo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_add);

        GetReadyWithDatabase();



        marks_marks =(TextInputLayout)findViewById(R.id.edt_task);

        dates=(Button)findViewById(R.id.date_selector_task);
        create_marks =findViewById(R.id.btn_create_task);
        cancel_marks =findViewById(R.id.btn_cancel_task);

        todokey = getIntent().getExtras().getString("todokey");
        flddate = getIntent().getExtras().getString("flddate");
        displaydate =getIntent().getExtras().getString("displaydate");
        todo = getIntent().getExtras().getString("todo");


        final String mdate[] =displaydate.split("/");

        marks_marks.getEditText().setText(todo);
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
                        TodoEditActivity.this,
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

                editRecord(todokey);

            }


        });
        cancel_marks.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                startActivity(new Intent(TodoEditActivity.this, TodoActivity.class));


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
        startActivity(new Intent(TodoEditActivity.this, TodoActivity.class));
        finish();
    }

    private void editRecord(String id) {







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



        Integer myfldkey = Integer.parseInt(id);

        ContentValues cv = new ContentValues();

        cv.put("todo",todoval );
        cv.put("flddate",dateval);
        cv.put("displaydate",displaydateval);


        String condition= "todokey=" + myfldkey;
        mydb.update(tablename, cv, condition, null);

        Toast.makeText(TodoEditActivity.this,"Sucessfully Updated",Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(TodoEditActivity.this, TodoActivity.class);
        startActivity(intent);

    }



}
