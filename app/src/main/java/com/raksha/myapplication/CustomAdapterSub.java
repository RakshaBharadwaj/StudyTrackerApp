package com.raksha.myapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapterSub extends RecyclerView.Adapter<CustomAdapterSub.MyViewHolder> {
    Context context;
    ArrayList sub_id,sub_name,max_credits;

    CustomAdapterSub(Context context,ArrayList sub_id,ArrayList sub_name,ArrayList max_credits){
        this.context=context;
        this.sub_id=sub_id;
        this.sub_name=sub_name;
        this.max_credits=max_credits;


    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.item_subject,parent,false);
        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.sub_id_txt.setText(String.valueOf(sub_id.get(position)));
        holder.sub_name_txt.setText(String.valueOf(sub_name.get(position)));
        holder.max_credits_txt.setText(String.valueOf(max_credits.get(position)));
        int id =Integer.parseInt(String.valueOf(sub_id.get(position)));
        holder.itemView.setTag(id);

        /*holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context,String.valueOf(test_id.get(position)) , Toast.LENGTH_SHORT).show();

                return false;
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return sub_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView sub_id_txt,sub_name_txt,max_credits_txt;
        View mView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            sub_id_txt=itemView.findViewById(R.id.sub_id_txt);
            sub_name_txt=itemView.findViewById(R.id.sub_name_txt);
            max_credits_txt=itemView.findViewById(R.id.max_credits_txt);
            mView=itemView;

        }



    }

}
