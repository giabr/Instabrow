package com.example.instabrow;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //Variables
    private FirebaseAuth mAuth;
    EditText emailView;
    EditText passwordView;
    Button signButton;
    TextView signMode;

    Boolean loginIsActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        emailView = findViewById(R.id.email);
        passwordView = findViewById(R.id.password);
        signButton = findViewById(R.id.signUp);
        signMode = findViewById(R.id.login);

        signMode.setOnClickListener(this);

        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otentifikasi(emailView.getText().toString(), passwordView.getText().toString());
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login){
            Log.i("Mode", "Change Signup Mode");
            if (loginIsActive){
                loginIsActive = false;
                signButton.setText("Sign In");
                signMode.setText("or, Sign Up");

            } else {
                loginIsActive = true;
                signButton.setText("Sign Up");
                signMode.setText("or, Sign In");

            }
        }
    }

    public void otentifikasi(String email, String pass){
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Username and Password required", Toast.LENGTH_SHORT).show();
        } else if (pass.length() < 6) {
            Toast.makeText(this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
        } else {
            if(loginIsActive){
                signUp(email, pass);
            } else {
                logIn(email, pass);
            };
        }

    }

    //Sign in
    public void logIn(final String email, final String pass){
        //authenticate user
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // there was an error
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this,"Succesfully Sign In", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //Sign up
    public void signUp(String email, String pass) {
                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.i("Status", "createUserWithEmail:success");
                                    Toast.makeText(MainActivity.this, "Account succesfully created", Toast.LENGTH_SHORT).show();
//                                FirebaseUser user = mAuth.getCurrentUser();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.i("Status", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }


