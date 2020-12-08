package com.raksha.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class MarksActivity extends AppCompatActivity {



    SQLiteDatabase mydb;
    String databasename = "mydatabase.db";
    String tablename = "mark_master";
    String tablename2="testmaster";
    String tablename3="submaster";
    Button addNew;
    CustomAdapterMarks customAdapterMarks;
    RecyclerView recyclerView;


    ArrayList<String> marks_id,test_id,sub_id,test_name,sub_name,marks,date,display_date;
    ArrayList<String> test_id_marks,test_name_marks,max_marks_marks;
    ArrayList<String> sub_id_marks, sub_name_marks, max_credits_marks;

    private androidx.appcompat.view.ActionMode mActionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks);

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
        stgs = stgs+ tablename3 + " (";
        stgs = stgs + "subkey integer primary key autoincrement,";
        stgs = stgs + "subname varchar(25),";
        stgs = stgs + "maxcredits integer);";


        mydb.execSQL(stgs);

        //addRecord();
        countRecords();


        recyclerView = findViewById(R.id.marks_recycler_view);
        addNew=findViewById(R.id.btn_add_marks);

        addNew.setOnClickListener(new View.OnClickListener() {
                                      public void onClick(View v) {
                                          Intent i = new Intent(MarksActivity.this, MarksAddActivity.class);
                                          startActivity(i);
                                      }
                                  }
        );




        marks_id=new ArrayList<>();
        test_id =new ArrayList<>();
        sub_id =new ArrayList<>();
        test_name =new ArrayList<>();
        sub_name =new ArrayList<>();
        marks=new ArrayList<>();
        date =new ArrayList<>();
        display_date =new ArrayList<>();

        test_id_marks =new ArrayList<>();
        test_name_marks =new ArrayList<>();
        max_marks_marks =new ArrayList<>();

        sub_id_marks = new ArrayList<>();
        sub_name_marks = new ArrayList<>();
        max_credits_marks = new ArrayList<>();

        Cursor ct;
        String stgt = "SELECT * FROM " + tablename3;
        ct= mydb.rawQuery(stgt, null);
        ct.moveToFirst();
        if (ct.getCount() == 0) {
            Toast.makeText(MarksActivity.this, "no subjects yet", Toast.LENGTH_SHORT).show();
        } else {
            do {
                sub_id_marks.add(ct.getString(0));
                sub_name_marks.add(ct.getString(1));
                max_credits_marks.add(ct.getString(2));

            } while (ct.moveToNext());
        }



        Cursor cf;
        String stgf  ="SELECT * FROM " + tablename2 ;
        cf  = mydb.rawQuery(stgf , null);
        cf.moveToFirst();
        if(cf.getCount()==0){
            Toast.makeText(MarksActivity.this,"no tests yet",Toast.LENGTH_SHORT).show();
        }else{
            do{
                test_id_marks.add(cf.getString(0));
                test_name_marks.add(cf.getString(1));
                max_marks_marks.add(cf.getString(2));

            }while(cf.moveToNext());
        }



        storeDataInArrays();
        customAdapterMarks = new CustomAdapterMarks(MarksActivity.this,marks_id,test_id,sub_id,test_name,sub_name,date,display_date,marks);
        recyclerView.setAdapter(customAdapterMarks);
        recyclerView.setLayoutManager(new LinearLayoutManager(MarksActivity.this));


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                switch(direction) {
                    case ItemTouchHelper.RIGHT:
                        RemoveItem((Integer) viewHolder.itemView.getTag());
                        break;
                    case ItemTouchHelper.LEFT:
                        EditItem((Integer) viewHolder.itemView.getTag());
                        break;
                }

            }



            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(MarksActivity.this,R.color.pink))
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(MarksActivity.this,R.color.pink))
                        .addSwipeRightActionIcon(R.drawable.delete)
                        .addSwipeLeftActionIcon(R.drawable.edit)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(recyclerView);


    }

    private void countRecords() {
        Cursor c;
        String stg  ="SELECT * FROM " + tablename ;
        c  = mydb.rawQuery(stg , null);
        c.moveToFirst();
        int count=c.getCount();

        Cursor ck;
        String stgk  ="SELECT * FROM " + tablename2 ;
        ck  = mydb.rawQuery(stgk , null);
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
        String stgs  ="SELECT * FROM " + tablename3 ;
        cs  = mydb.rawQuery(stgs, null);
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



    }



    public void onBackPressed() {
        startActivity(new Intent(MarksActivity.this, MainActivity2.class));
        finish();
    }

    private void addRecord() {
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
        String dateval="22/09/20";
        String displaydateval ="22/09/20";
        int marksval=30;

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

        mydb.execSQL(stg);

    }

    private void RemoveItem(final int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(MarksActivity.this);

        View view = LayoutInflater.from(MarksActivity.this).inflate(R.layout.custom_layout, null);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                mydb.execSQL("DELETE FROM " + tablename + " WHERE markskey=" + id);
                storeDataInArrays();
                Intent intent =new Intent(MarksActivity.this,MarksActivity.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent =new Intent(MarksActivity.this,MarksActivity.class);
                startActivity(intent);
            }
        });

        builder.setView(view);
        builder.show();




    }

    private void EditItem(int id) {

        String stg = "SELECT * FROM " + tablename + " WHERE markskey="+id;
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
            Toast.makeText(MarksActivity.this, "No. of Entries  : " + countrecords, Toast.LENGTH_LONG).show();

            finish();
        }
        String markskey="";
        String testkey="";
        String subkey="";
        String testname="";
        String subname="";
        String flddate="";
        String displaydate="";
        String marks="";

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

            c.moveToNext();
        }
        c.close();

       Intent intent =new Intent(MarksActivity.this,MarksEditActivity.class);
        intent.putExtra("markskey",markskey);
        intent.putExtra("testkey",testkey);
        intent.putExtra("subkey",subkey);
        intent.putExtra("testname",testname);
        intent.putExtra("subname",subname);
        intent.putExtra("flddate",flddate);
        intent.putExtra("displaydate",displaydate);
        intent.putExtra("marks",marks);
        startActivity(intent);


    }


    void storeDataInArrays(){
        Cursor c;
        String stg  ="SELECT * FROM " + tablename ;
        c  = mydb.rawQuery(stg , null);
        c.moveToFirst();
        if(c.getCount()==0){
            Toast.makeText(MarksActivity.this,"no Marks yet",Toast.LENGTH_SHORT).show();
        }else{
            do{
                marks_id.add(c.getString(0));
                test_id.add(c.getString(1));
                sub_id.add(c.getString(2));
                test_name.add(c.getString(3));
                sub_name.add(c.getString(4));
                date.add(c.getString(5));
                display_date.add(c.getString(6));
                marks.add(c.getString(7));


            }while(c.moveToNext());
        }
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

    Cursor readAllData(){
        Cursor c;
        String stg  ="SELECT * FROM " + tablename ;
        c  = mydb.rawQuery(stg , null);
        c.moveToFirst();
        return c;

    }



}