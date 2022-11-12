package com.example.lostandfound;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private Button resetpw;
    private Button back;
    private ProgressBar progressBar;
    FirebaseAuth auth;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        auth= FirebaseAuth.getInstance();

        email= (EditText) findViewById(R.id.forgotMail);

        resetpw=(Button) findViewById((R.id.forgotButton));
        resetpw.setOnClickListener(this);

        back= (Button) findViewById(R.id.forgotBack);
        back.setOnClickListener(ForgotPassword.this);

        progressBar=(ProgressBar) findViewById(R.id.forgotprogressBar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.forgotButton:
                resetpass();
                break;
            case R.id.forgotBack:
                startActivity(new Intent(ForgotPassword.this, MainActivity.class));
                break;
        }

    }

    private void resetpass() {
        String mail= email.getText().toString().trim();
        if (mail.isEmpty()) {
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }
        //checking email
        if (!mail.endsWith("@iitp.ac.in") || mail.startsWith("@iitp.ac.in")) {
            email.setError("Enter valid Email ID");
            email.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Reset link sent to email successfully.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgotPassword.this, MainActivity.class));
                }
                else{
                    Toast.makeText(ForgotPassword.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}