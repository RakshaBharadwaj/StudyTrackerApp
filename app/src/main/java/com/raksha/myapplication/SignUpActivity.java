package com.raksha.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignUpActivity extends AppCompatActivity {
    private TextInputLayout editTextName, editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button SignIn;
    private FirebaseAuth mAuth;
    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    public static String temp_stg_name,temp_stg_email,temp_stg_password;
    private String stg_name,stg_email,stg_password,stg_confirmPassword;

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(SignUpActivity.this,MainActivity2.class);
            startActivity(intent);
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);


        editTextName = (TextInputLayout) findViewById(R.id.edit_text_name);
        editTextEmail = (TextInputLayout) findViewById(R.id.edit_text_emailId);
        editTextPassword = (TextInputLayout) findViewById(R.id.edit_text_password);
        editTextConfirmPassword = (TextInputLayout) findViewById(R.id.edit_text_confirm_password);


        mAuth = FirebaseAuth.getInstance();




        SignIn = (Button) findViewById(R.id.btnSignIn);
        SignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                registerUser();
            }
        });



        createRequest();


        findViewById(R.id.google_signIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                

                signIn();

            }
        });


    }

    private void createRequest() {


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(this, "No Internet..!", Toast.LENGTH_SHORT).show();
            }
        }
    }



        public void updateUI (FirebaseUser account){
            if (account != null) {
                FirebaseUser user = mAuth.getCurrentUser();
                Intent intent = new Intent(SignUpActivity.this, MainActivity2.class);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Sorry,we couldn't sign you in", Toast.LENGTH_LONG).show();
            }
        }




        public void registerUser(){
             stg_name = editTextName.getEditText().getText().toString().trim();
             stg_email = editTextEmail.getEditText().getText().toString().trim();
             stg_password = editTextPassword.getEditText().getText().toString().trim();
             stg_confirmPassword = editTextConfirmPassword.getEditText().getText().toString().trim();

            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if (stg_name.isEmpty()) {
                editTextName.setError("Name Required");
                editTextName.requestFocus();
                return;
            } else {
                editTextName.setErrorEnabled(false);
            }
            if (stg_email.isEmpty()) {
                editTextEmail.setError("Email Id Required");
                editTextEmail.requestFocus();
                return;

            } else {
                editTextEmail.setErrorEnabled(false);
            }


            if (!stg_email.matches(emailPattern)) {
                editTextEmail.setError("Enter a Valid Email");
                editTextEmail.requestFocus();
                return;

            } else {
                editTextEmail.setErrorEnabled(false);
            }
            if (stg_password.isEmpty()) {
                editTextPassword.setError("password Required");
                editTextPassword.requestFocus();
                return;


            } else {
                editTextPassword.setErrorEnabled(false);
            }
            if (stg_confirmPassword.isEmpty()) {
                editTextConfirmPassword.setError("Confirm password");
                editTextConfirmPassword.requestFocus();
                return;
            } else {
                editTextConfirmPassword.setErrorEnabled(false);
            }
            if (!stg_password.equals(stg_confirmPassword)) {
                editTextConfirmPassword.setError("Passwords don't match");
                editTextConfirmPassword.requestFocus();
                return;
            } else {
                editTextConfirmPassword.setErrorEnabled(false);
            }


            mAuth.createUserWithEmailAndPassword(stg_email, stg_password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("ok", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("ok", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            SharedPreferences sp = getSharedPreferences("FILE_NAME", MODE_PRIVATE);
                            SharedPreferences.Editor edit = sp.edit();
                            edit.putString("key",stg_name);
                            edit.apply();

                            // ...
                        }
                    });




        }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(SignUpActivity.this, MainActivity2.class);
                            startActivity(intent);


                        } else {
                            Toast.makeText(SignUpActivity.this, "Sorry,we couldn't sign you in", Toast.LENGTH_SHORT).show();


                        }


                    }
                });
    }
}