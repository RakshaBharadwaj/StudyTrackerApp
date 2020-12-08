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

public class CustomAdapterMarks extends RecyclerView.Adapter<CustomAdapterMarks.MyViewHolder> {
    Context context;

    ArrayList marks_id,test_id,sub_id,test_name,sub_name,marks,date,display_date;



  CustomAdapterMarks(Context context, ArrayList marks_id, ArrayList test_id, ArrayList sub_id,ArrayList test_name,ArrayList sub_name, ArrayList<String> date,ArrayList display_date, ArrayList<String> marks) {
        this.context=context;
        this.marks_id=marks_id;
        this.test_id=test_id;
        this.sub_id=sub_id;
        this.test_name=test_name;
        this.sub_name=sub_name;
        this.marks=marks;
        this.date=date;
        this.display_date=display_date;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_marks,parent,false);
        return new MyViewHolder(view);
    }
 @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.test_id_txt.setText(String.valueOf (test_id.get(position)));
        holder.sub_id_txt.setText(String.valueOf (sub_id.get(position)));
        holder.test_name_txt.setText(String.valueOf (test_name.get(position)));
        holder.sub_name_txt.setText(String.valueOf(sub_name.get(position)));
        holder.date_display_txt.setText(String.valueOf(display_date.get(position)));
        holder.date_txt.setText(String.valueOf(date.get(position)));
        holder.marks_txt.setText(String.valueOf(marks.get(position)));

        int id =Integer.parseInt(String.valueOf(marks_id.get(position)));

        holder.itemView.setTag(id);



    }
    @Override
    public int getItemCount() {
        return marks_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView marks_id_txt, test_id_txt ,sub_id_txt, test_name_txt ,sub_name_txt,marks_txt,date_display_txt,date_txt;
        View mView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            marks_id_txt=itemView.findViewById(R.id.marks_id_txt);
            test_id_txt=itemView.findViewById(R.id.test_id_marks_txt);
            sub_id_txt=itemView.findViewById(R.id.sub_id_marks_txt);
            test_name_txt=itemView.findViewById(R.id.test_name_marks_txt);
            sub_name_txt=itemView.findViewById(R.id.sub_name_marks_txt);
            marks_txt=itemView.findViewById(R.id.marks_txt);
            date_display_txt=itemView.findViewById(R.id.date_marks_txt);
            date_txt=itemView.findViewById(R.id.date_txt);
            mView=itemView;

        }



    }

}
