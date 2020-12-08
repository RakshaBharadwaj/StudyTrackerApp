package com.raksha.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class SubjectAddActivity extends AppCompatActivity {
    TextInputLayout sub_title,max_credits;
    Button create,cancel;
    SQLiteDatabase mydb;
    String databasename = "mydatabase.db";
    String tablename = "submaster";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_add);

        sub_title =(TextInputLayout)findViewById(R.id.edt_sub_name);
        max_credits =(TextInputLayout)findViewById(R.id.edt_max_credits);

        create =findViewById(R.id.btn_create_sub);
        cancel =findViewById(R.id.btn_cancel_sub);

        GetReadyWithDatabase();



        create.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                addRecord();



            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(SubjectAddActivity.this, SubjectActivity.class);
                startActivity(intent);

            }
        });
    }
    public void GetReadyWithDatabase() {
        mydb = this.openOrCreateDatabase(databasename, MODE_PRIVATE, null);

        String stg = "CREATE TABLE IF NOT EXISTS ";
        stg = stg + tablename + " (";
        stg = stg + "subkey integer primary key autoincrement,";
        stg= stg  + "subname varchar(25),";
        stg = stg + "maxcredits integer);";


        mydb.execSQL(stg);



    }

    public void onBackPressed() {
        startActivity(new Intent(SubjectAddActivity.this, SubjectActivity.class));
        finish();
    }

    private void addRecord() {
        String subkey="subkey";
        String subname="subname";
        String maxcredits="maxcredits";

        String stg_sub_name = sub_title.getEditText().getText().toString().trim();
        String stg_max_credits = max_credits.getEditText().getText().toString().trim();



        if (stg_sub_name.isEmpty()) {
            sub_title.setError("Subject Name Required");
            sub_title.requestFocus();
            return;

        } else {
            sub_title.setErrorEnabled(false);
        }



        if (stg_max_credits.isEmpty()) {
            max_credits.setError("Maximum Credits Required");
            max_credits.requestFocus();

            return;

        } else {
            max_credits.setErrorEnabled(false);
        }
        if (stg_max_credits.length()>1) {
            max_credits.setError("Enter valid Credits");
            max_credits.requestFocus();
            return;
        } else {
           max_credits.setErrorEnabled(false);
        }

        String subnameval=sub_title.getEditText().getText().toString().trim();
        int maxcreditsval= Integer.parseInt(max_credits.getEditText().getText().toString().trim());

        String stg;
        stg = "INSERT INTO  " + tablename  + "(" ;

        stg = stg  + subname + ",";
        stg = stg + maxcredits +  ")";

        stg = stg + "  VALUES (" ;
        stg = stg +"'"+  subnameval  + "',";

        stg = stg +  maxcreditsval + ");";

        mydb.execSQL(stg);
        Intent intent = new Intent(SubjectAddActivity.this, SubjectActivity.class);
        startActivity(intent);

    }
}