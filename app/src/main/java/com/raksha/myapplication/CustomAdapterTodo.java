package com.raksha.myapplication;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapterTodo extends RecyclerView.Adapter<CustomAdapterTodo.MyViewHolder> {
    Context context;

    ArrayList todo_id,todo,date,display_date;



    CustomAdapterTodo(Context context, ArrayList todo_id, ArrayList todo, ArrayList date, ArrayList display_date) {
        this.context=context;
        this.todo_id=todo_id;
        this.todo=todo;
        this.date=date;
        this.display_date=display_date;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_todo,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.todo_id_txt.setText(String.valueOf (todo_id.get(position)));
        holder.todo_txt.setText(String.valueOf(todo.get(position)));
        holder.date_display_txt.setText(String.valueOf(display_date.get(position)));
        holder.date_txt.setText(String.valueOf(date.get(position)));


        int id =Integer.parseInt(String.valueOf(todo_id.get(position)));

        holder.itemView.setTag(id);



    }
    @Override
    public int getItemCount() {
        return todo_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView todo_id_txt,todo_txt,date_display_txt,date_txt;
        View mView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            todo_id_txt=itemView.findViewById(R.id.todo_id_txt);
            todo_txt=itemView.findViewById(R.id.todo_txt);
            date_display_txt=itemView.findViewById(R.id.date_todo_txt);
            date_txt=itemView.findViewById(R.id.date_real_todo_txt);
            mView=itemView;

        }



    }

}

