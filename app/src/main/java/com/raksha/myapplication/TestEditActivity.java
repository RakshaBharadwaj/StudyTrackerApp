package com.raksha.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class TestEditActivity extends AppCompatActivity {
    TextInputLayout test_title,max_marks;
    Button create,cancel;
    SQLiteDatabase mydb;
    String databasename = "mydatabase.db";
    String tablename = "testmaster";






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_add);

        test_title =(TextInputLayout)findViewById(R.id.edt_test_name);
        max_marks =(TextInputLayout)findViewById(R.id.edt_max_marks);

        create =findViewById(R.id.btn_create_test);
        cancel =findViewById(R.id.btn_cancel);

        GetReadyWithDatabase();

        final String testid = String.valueOf(getIntent().getExtras().getString("testkey"));
        String testname = String.valueOf(getIntent().getExtras().getString("testname"));
        String maxmarks = String.valueOf(getIntent().getExtras().getString("maxmarks"));

        test_title.getEditText().setText(testname);
        max_marks.getEditText().setText(maxmarks);

        create.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                editRecord(testid);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(TestEditActivity.this, TestsActivity.class);
                startActivity(intent);

            }
        });
    }
    public void GetReadyWithDatabase() {
        mydb = this.openOrCreateDatabase(databasename, MODE_PRIVATE, null);

        String stg = "CREATE TABLE IF NOT EXISTS ";
        stg = stg + tablename + " (";
        stg = stg + "testkey integer primary key autoincrement,";
        stg= stg  + "testname varchar(25),";
        stg = stg + "maxmarks integer);";


        mydb.execSQL(stg);



    }

    public void onBackPressed() {
        startActivity(new Intent(TestEditActivity.this, TestsActivity.class));
        finish();
    }

    private void editRecord(String id) {
        String testkey="testkey";
        String testname="testname";
        String maxmarks="maxmarks";

        String stg_test_name = test_title.getEditText().getText().toString().trim();
        String stg_max_marks = max_marks.getEditText().getText().toString().trim();



        if (stg_test_name.isEmpty()) {
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
        }

        String testnameval= test_title.getEditText().getText().toString().trim();
        int maxmarksval= Integer.parseInt(max_marks.getEditText().getText().toString().trim()) ;


        Integer myfldkey = Integer.parseInt(id);

        ContentValues cv = new ContentValues();

        cv.put("testname",testnameval );
        cv.put("maxmarks",maxmarksval);



        String condition= " testkey=" + myfldkey;
        mydb.update(tablename, cv, condition, null);

        Toast.makeText(TestEditActivity.this,"Sucessfully Updated",Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(TestEditActivity.this, TestsActivity.class);
        startActivity(intent);

    }
}