package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private EditText editemail;
    private EditText editpw;
    private Button login;
    private FirebaseAuth mAuth;
    private ProgressBar loading;
    private TextView resetPw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editemail=(EditText)findViewById(R.id.openUsername) ;
        editpw= (EditText)findViewById(R.id.openPass);

        login= (Button)findViewById(R.id.openLogin);
        login.setOnClickListener(this);

        resetPw=(TextView)findViewById((R.id.openForgotpw));
        resetPw.setOnClickListener(this);

        loading= (ProgressBar)findViewById(R.id.openprogressBar);
        register= (TextView)findViewById(R.id.openRegister);
        register.setOnClickListener(this);
        mAuth= FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.openRegister:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.openLogin:
                userLogin();
                break;
            case R.id.openForgotpw:
                startActivity(new Intent(MainActivity.this, ForgotPassword.class));
                break;


        }
    }


    private void userLogin() {
        String username, pass;
        username= editemail.getText().toString().trim();
        pass= editpw.getText().toString();

        if(username.isEmpty())
        {
            editemail.setError("Enter Institute Email");
            editemail.requestFocus();
            return;
        }
        if(pass.isEmpty())
        {
            editpw.setError("Enter Password");
            editpw.requestFocus();
            return;
        }
        loading.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(username, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                    //login successful open user profile
                    startActivity(new Intent(MainActivity.this, HomePage.class));}
                    else{
//                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Please Verify Your Email", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
                loading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user= mAuth.getCurrentUser();
        if(user!= null){
            startActivity(new Intent(this, HomePage.class));
        }

    }
}
