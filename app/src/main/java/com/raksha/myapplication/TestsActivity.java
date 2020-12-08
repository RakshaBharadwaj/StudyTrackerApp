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
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class TestsActivity extends AppCompatActivity {



    SQLiteDatabase mydb;
    String databasename = "mydatabase.db";
    String tablename = "testmaster";
    Button addNew;
    CustomAdapter customAdapter;
    RecyclerView recyclerView;


    ArrayList<String> test_id,test_name,max_marks;

    private androidx.appcompat.view.ActionMode mActionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);





        GetReadyWithDatabase();
        //addRecord();
        countRecords();


        recyclerView =findViewById(R.id.test_recycler_view);
        addNew=findViewById(R.id.btn_add_new);

        addNew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(TestsActivity.this, TestAddActivity.class);
                startActivity(i);
            }
        }
        );





        test_id =new ArrayList<>();
        test_name =new ArrayList<>();
        max_marks =new ArrayList<>();
        storeDataInArrays();
        customAdapter = new CustomAdapter(TestsActivity.this,test_id,test_name,max_marks);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(TestsActivity.this));



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
                       .addSwipeRightBackgroundColor(ContextCompat.getColor(TestsActivity.this,R.color.pink))
                       .addSwipeLeftBackgroundColor(ContextCompat.getColor(TestsActivity.this,R.color.pink))
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

        if (count==0){
            addRecord();
        }

    }

    public void onBackPressed() {
        startActivity(new Intent(TestsActivity.this, MainActivity2.class));
        finish();
    }

    private void addRecord() {
        String testkey="testkey";
        String testname="testname";
        String maxmarks="maxmarks";

        String testnameval="SEE";
        int maxmarksval=100;

        String stg;
        stg = "INSERT INTO  " + tablename  + "(" ;

        stg = stg  + testname + ",";
        stg = stg + maxmarks +  ")";

        stg = stg + "  VALUES (" ;
        stg = stg +"'"+  testnameval  + "',";

        stg = stg +  maxmarksval + ");";

        mydb.execSQL(stg);
         testnameval="CIE";
         maxmarksval=20;
        String stgk;
        stgk = "INSERT INTO  " + tablename  + "(" ;

        stgk = stgk  + testname + ",";
        stgk = stgk + maxmarks +  ")";

        stgk = stgk + "  VALUES (" ;
        stgk = stgk +"'"+  testnameval  + "',";

        stgk = stgk +  maxmarksval + ");";

        mydb.execSQL(stgk);


    }

    private void RemoveItem(final int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(TestsActivity.this);

        View view = LayoutInflater.from(TestsActivity.this).inflate(R.layout.custom_layout, null);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                mydb.execSQL("DELETE FROM " + tablename + " WHERE testkey=" + id);
                storeDataInArrays();
                Intent intent =new Intent(TestsActivity.this,TestsActivity.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent =new Intent(TestsActivity.this,TestsActivity.class);
                startActivity(intent);
            }
        });

        builder.setView(view);
        builder.show();




    }

    private void EditItem(int id) {
        Cursor c;
        String stg = "SELECT * FROM " + tablename + " WHERE testkey=" + id;
        c  = mydb.rawQuery(stg, null);
        c.moveToLast();

        int Column1 = c.getColumnIndex("testkey");
        int Column2 = c.getColumnIndex("testname");
        int Column3 = c.getColumnIndex("maxmarks");
        c.moveToFirst();

        int countrecords = c.getCount();
        if(countrecords==0)
        {
            Toast.makeText(TestsActivity.this, "No. of Entries  : " + countrecords, Toast.LENGTH_LONG).show();

            finish();
        }
        String testkey="";
        String testname="";
        String maxmarks="";

        for (int i=1;i<=countrecords;i++)
        {
            testkey =  c.getString(Column1);
            testname = c.getString(Column2);
            maxmarks = c.getString(Column3);

            c.moveToNext();
        }
        c.close();




        Intent i =new Intent(TestsActivity.this,TestEditActivity.class);
        i.putExtra("testkey",testkey);
        i.putExtra("testname",testname);
        i.putExtra("maxmarks",maxmarks);
        startActivity(i);


    }


    void storeDataInArrays(){
        Cursor c;
        String stg  ="SELECT * FROM " + tablename ;
        c  = mydb.rawQuery(stg , null);
        c.moveToFirst();
        if(c.getCount()==0){
            Toast.makeText(TestsActivity.this,"no tests yet",Toast.LENGTH_SHORT).show();
        }else{
           do{
                test_id.add(c.getString(0));
                test_name.add(c.getString(1));
                max_marks.add(c.getString(2));

            }while(c.moveToNext());
        }
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

    Cursor readAllData(){
        Cursor c;
        String stg  ="SELECT * FROM " + tablename ;
        c  = mydb.rawQuery(stg , null);
        c.moveToFirst();
        return c;

    }



}