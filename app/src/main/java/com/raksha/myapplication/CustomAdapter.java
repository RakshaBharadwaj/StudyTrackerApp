package com.raksha.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    Context context;
    ArrayList test_id,test_name,max_marks;

    CustomAdapter(Context context,ArrayList test_id, ArrayList test_name, ArrayList max_marks){
        this.context=context;
        this.test_id=test_id;
        this.test_name=test_name;
        this.max_marks=max_marks;


    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.item_tests,parent,false);
        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.test_id_txt.setText(String.valueOf(test_id.get(position)));
        holder.test_name_txt.setText(String.valueOf(test_name.get(position)));
        holder.max_marks_txt.setText(String.valueOf(max_marks.get(position)));
        int id =Integer.parseInt(String.valueOf(test_id.get(position)));
        holder.itemView.setTag(id);


    }

    @Override
    public int getItemCount() {
        return test_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView test_id_txt,test_name_txt,max_marks_txt;
        View mView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            test_id_txt=itemView.findViewById(R.id.test_id_txt);
            test_name_txt=itemView.findViewById(R.id.test_name_txt);
            max_marks_txt=itemView.findViewById(R.id.max_marks_txt);
            mView=itemView;

        }



    }

}
