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

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class SubjectActivity extends AppCompatActivity {


    SQLiteDatabase mydb;
    String databasename = "mydatabase.db";
    String tablename = "submaster";
    Button addNewSub;
    CustomAdapterSub customAdapternew;
    RecyclerView recyclerView;


    ArrayList<String> sub_id, sub_name, max_credits;

    private androidx.appcompat.view.ActionMode mActionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);


        GetReadyWithDatabase();
        //addRecord();
        countRecords();


        recyclerView = findViewById(R.id.sub_recycler_view);
        addNewSub = findViewById(R.id.btn_add_new_sub);

        addNewSub.setOnClickListener(new View.OnClickListener() {
                                      public void onClick(View v) {
                                          Intent i = new Intent(SubjectActivity.this, SubjectAddActivity.class);
                                          startActivity(i);
                                      }
                                  }
        );


        sub_id = new ArrayList<>();
        sub_name = new ArrayList<>();
        max_credits = new ArrayList<>();
        storeDataInArrays();
        customAdapternew = new CustomAdapterSub(SubjectActivity.this,sub_id,sub_name,max_credits);
        recyclerView.setAdapter(customAdapternew);
        recyclerView.setLayoutManager(new LinearLayoutManager(SubjectActivity.this));


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                switch (direction) {
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
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(SubjectActivity.this, R.color.pink))
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(SubjectActivity.this, R.color.pink))
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
        String stg = "SELECT * FROM " + tablename;
        c = mydb.rawQuery(stg, null);
        c.moveToFirst();
        int count = c.getCount();

        if (count==0){
            addRecord();
        }

    }

    public void onBackPressed() {
        startActivity(new Intent(SubjectActivity.this, MainActivity2.class));
        finish();
    }

    private void addRecord() {
        String subkey = "subkey";
        String subname = "subname";
        String maxcredits = "maxcredits";

        String subnameval = "Subject";
        int maxcreditsval = 4;

        String stg;
        stg = "INSERT INTO  " + tablename + "(";

        stg = stg + subname + ",";
        stg = stg + maxcredits + ")";

        stg = stg + "  VALUES (";
        stg = stg + "'" + subnameval + "',";

        stg = stg + maxcreditsval + ");";

        mydb.execSQL(stg);

    }

    private void RemoveItem(final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SubjectActivity.this);

        View view = LayoutInflater.from(SubjectActivity.this).inflate(R.layout.custom_layout, null);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                mydb.execSQL("DELETE FROM " + tablename + " WHERE subkey=" + id);
                storeDataInArrays();
                Intent intent = new Intent(SubjectActivity.this, SubjectActivity.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(SubjectActivity.this, SubjectActivity.class);
                startActivity(intent);
            }
        });

        builder.setView(view);
        builder.show();


    }

    private void EditItem(int id) {
        Cursor c;
        String stg = "SELECT * FROM " + tablename + " WHERE subkey=" + id;
        c = mydb.rawQuery(stg, null);
        c.moveToLast();

        int Column1 = c.getColumnIndex("subkey");
        int Column2 = c.getColumnIndex("subname");
        int Column3 = c.getColumnIndex("maxcredits");
        c.moveToFirst();

        int countrecords = c.getCount();
        if (countrecords == 0) {
            Toast.makeText(SubjectActivity.this, "No. of Entries  : " + countrecords, Toast.LENGTH_LONG).show();

            finish();
        }
        String subkey = "";
        String subname = "";
        String maxcredits = "";

        for (int i = 1; i <= countrecords; i++) {
            subkey = c.getString(Column1);
            subname = c.getString(Column2);
            maxcredits = c.getString(Column3);

            c.moveToNext();
        }
        c.close();


        Intent i = new Intent(SubjectActivity.this, SubjectEditActivity.class);
        i.putExtra("subkey", subkey);
        i.putExtra("subname", subname);
        i.putExtra("maxcredits", maxcredits);
        startActivity(i);


    }


    void storeDataInArrays() {
        Cursor c;
        String stg = "SELECT * FROM " + tablename;
        c = mydb.rawQuery(stg, null);
        c.moveToFirst();
        if (c.getCount() == 0) {
            Toast.makeText(SubjectActivity.this, "no subjects yet", Toast.LENGTH_SHORT).show();
        } else {
            do {
                sub_id.add(c.getString(0));
                sub_name.add(c.getString(1));
                max_credits.add(c.getString(2));

            } while (c.moveToNext());
        }
    }


    public void GetReadyWithDatabase() {
        mydb = this.openOrCreateDatabase(databasename, MODE_PRIVATE, null);

        String stg = "CREATE TABLE IF NOT EXISTS ";
        stg = stg + tablename + " (";
        stg = stg + "subkey integer primary key autoincrement,";
        stg = stg + "subname varchar(25),";
        stg = stg + "maxcredits integer);";


        mydb.execSQL(stg);


    }
}