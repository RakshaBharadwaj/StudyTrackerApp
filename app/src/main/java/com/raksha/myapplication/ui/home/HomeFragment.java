package com.raksha.myapplication.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.raksha.myapplication.AccountActivity;
import com.raksha.myapplication.ChartActivity;
import com.raksha.myapplication.MainActivity2;
import com.raksha.myapplication.MarksActivity;
import com.raksha.myapplication.MarksAddActivity;
import com.raksha.myapplication.R;
import com.raksha.myapplication.SignUpActivity;
import com.raksha.myapplication.SubjectActivity;
import com.raksha.myapplication.TestsActivity;
import com.raksha.myapplication.TodoActivity;

public class HomeFragment extends Fragment implements View.OnClickListener{
    public boolean test_open,sub_open;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        LinearLayout myLyt =(LinearLayout)root.findViewById(R.id.linear_lyt_test);
        LinearLayout myChart=(LinearLayout)root.findViewById(R.id.linear_lyt_chart);
        LinearLayout mySub=(LinearLayout)root.findViewById(R.id.linear_lyt_sub);
        LinearLayout myAcc=(LinearLayout)root.findViewById(R.id.linear_lyt_acc);
        LinearLayout myCharts =root.findViewById(R.id.Linear_lyt_charts);
        LinearLayout myTodo =root.findViewById(R.id.linear_lyt_todo);
        myLyt.setOnClickListener(this);
        myChart.setOnClickListener(this);
        mySub.setOnClickListener(this);
        myAcc.setOnClickListener(this);
        myCharts.setOnClickListener(this);
        myTodo.setOnClickListener(this);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    @Override
    public void onClick(View v) {


        switch(v.getId()){
            case R.id.linear_lyt_test:{
                test_open=true;
                Intent intent = new Intent(getContext(), TestsActivity.class);
                startActivity(intent);

                break;}

            case R.id.linear_lyt_sub: {
                sub_open = true;
                Intent i = new Intent(getContext(), SubjectActivity.class);
                startActivity(i);

                break;
            }
            case R.id.linear_lyt_chart: {
                    Intent ik = new Intent(getContext(), MarksActivity.class);
                    startActivity(ik);
                    break;

            }

                case R.id.Linear_lyt_charts: {
                    Intent is = new Intent(getContext(), ChartActivity.class);
                    startActivity(is);
                    break;
                }

            case R.id.linear_lyt_acc: {
                Intent ir = new Intent(getContext(), AccountActivity.class);
                startActivity(ir);
                break;
            }
            case R.id.linear_lyt_todo: {
                Intent irt = new Intent(getContext(), TodoActivity.class);
                startActivity(irt);
                break;
            }
        }

    }
}