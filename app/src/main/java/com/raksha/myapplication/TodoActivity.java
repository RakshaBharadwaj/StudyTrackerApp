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


public class TodoActivity extends AppCompatActivity {


    SQLiteDatabase mydb;
    String databasename = "mydatabase.db";
    String tablename = "todo_master";

    Button addNew;
    CustomAdapterTodo customAdapterTodo;
    RecyclerView recyclerView;


    ArrayList<String> todo_id, todo,date, display_date;


    private androidx.appcompat.view.ActionMode mActionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        GetReadyWithDatabase();

        countRecords();


        recyclerView = findViewById(R.id.tasks_recycler_view);
        addNew = findViewById(R.id.btn_add_tasks);

        addNew.setOnClickListener(new View.OnClickListener() {
                                      public void onClick(View v) {
                                          Intent i = new Intent(TodoActivity.this, TodoAddActivity.class);
                                          startActivity(i);
                                      }
                                  }
        );


        todo_id = new ArrayList<>();
        todo = new ArrayList<>();
        date = new ArrayList<>();
        display_date = new ArrayList<>();





        storeDataInArrays();
        customAdapterTodo = new CustomAdapterTodo(TodoActivity.this, todo_id, todo, date, display_date);
        recyclerView.setAdapter(customAdapterTodo);
        recyclerView.setLayoutManager(new LinearLayoutManager(TodoActivity.this));


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
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(TodoActivity.this, R.color.pink))
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(TodoActivity.this, R.color.pink))
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


    }


    public void onBackPressed() {
        startActivity(new Intent(TodoActivity.this, MainActivity2.class));
        finish();
    }



    private void RemoveItem(final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TodoActivity.this);

        View view = LayoutInflater.from(TodoActivity.this).inflate(R.layout.custom_layout, null);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                mydb.execSQL("DELETE FROM " + tablename + " WHERE todokey=" + id);
                storeDataInArrays();
                Intent intent = new Intent(TodoActivity.this, TodoActivity.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(TodoActivity.this,TodoActivity.class);
                startActivity(intent);
            }
        });

        builder.setView(view);
        builder.show();


    }

    private void EditItem(int id) {

        String stg = "SELECT * FROM " + tablename + " WHERE todokey=" + id;
        Cursor c = mydb.rawQuery(stg, null);
        c.moveToLast();

        int Column1 = c.getColumnIndex("todokey");
        int Column2 = c.getColumnIndex("todo");
        int Column3 = c.getColumnIndex("flddate");
        int Column4 = c.getColumnIndex("displaydate");

        c.moveToFirst();

        int countrecords = c.getCount();
        if (countrecords == 0) {
            Toast.makeText(TodoActivity.this, "No. of Entries  : " + countrecords, Toast.LENGTH_LONG).show();

            finish();
        }
        String todokey = "";
        String todo = "";
        String flddate = "";
        String displaydate = "";


        for (int i = 1; i <= countrecords; i++) {
            todokey = c.getString(Column1);
            todo = c.getString(Column2);
            flddate = c.getString(Column3);
            displaydate = c.getString(Column4);


            c.moveToNext();
        }
        c.close();

        Intent intent = new Intent(TodoActivity.this, TodoEditActivity.class);
        intent.putExtra("todokey", todokey);
        intent.putExtra("todo", todo);
        intent.putExtra("flddate", flddate);
        intent.putExtra("displaydate", displaydate);

        startActivity(intent);


    }


    void storeDataInArrays() {
        Cursor c;
        String stg = "SELECT * FROM " + tablename;
        c = mydb.rawQuery(stg, null);
        c.moveToFirst();
        if (c.getCount() == 0) {
            Toast.makeText(TodoActivity.this, "no Tasks yet", Toast.LENGTH_SHORT).show();
        } else {
            do {
                todo_id.add(c.getString(0));
                todo.add(c.getString(1));

                date.add(c.getString(2));
                display_date.add(c.getString(3));



            } while (c.moveToNext());
        }
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




}