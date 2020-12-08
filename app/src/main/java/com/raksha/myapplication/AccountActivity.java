package com.raksha.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        TextView name =  findViewById(R.id.name_display_act);

        TextView email =  findViewById(R.id.email_display_act);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(AccountActivity.this);
        if(signInAccount != null){
            name.setText(signInAccount.getDisplayName());
            email.setText(signInAccount.getEmail());

        }else{
            if( user != null) {
                SharedPreferences sp = getSharedPreferences("FILE_NAME", MODE_PRIVATE);
                String email_name  = sp.getString("key", null);
                String email_email= user.getEmail();
                name.setText(email_name);
                email.setText(email_email);
            }

        }
    }
    public void onBackPressed() {
        startActivity(new Intent(AccountActivity.this, MainActivity2.class));
        finish();
    }
}