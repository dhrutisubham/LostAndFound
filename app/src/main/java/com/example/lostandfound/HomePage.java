package com.example.lostandfound;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HomePage extends AppCompatActivity implements View.OnClickListener{


    private Button logout, lost, found,replost, repfound, posts, udPass;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private TextView roll, name;
    private ImageView dp;
    private DatabaseReference databaseReference;
    private String uName, uRoll, urlDP;
    private StorageReference storageReference, imgpath;

//    public HomePage(Context context){
//        this.context=context;
//    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        logout=(Button) findViewById(R.id.homeLogout);
        logout.setOnClickListener(this);

        lost= (Button)findViewById(R.id.lostFeed);
        lost.setOnClickListener(this);

        found=(Button)findViewById(R.id.foundFeed);
        found.setOnClickListener(this);

        replost=(Button)findViewById(R.id.lostItem);
        replost.setOnClickListener(this);

        repfound=(Button)findViewById(R.id.foundItem);
        repfound.setOnClickListener(this);

        posts=(Button)findViewById(R.id.myPosts);
        posts.setOnClickListener(this);

        udPass= (Button)findViewById(R.id.updatePass);
        udPass.setOnClickListener(this);


        mAuth= FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        storageReference= FirebaseStorage.getInstance().getReference();


        user= FirebaseAuth.getInstance().getCurrentUser();

        roll= (TextView) findViewById(R.id.homeRoll);
        name= (TextView) findViewById(R.id.homeName);
        dp= (ImageView) findViewById(R.id.homeDP);


        uRoll= user.getUid();
//        Toast.makeText(HomePage.this, uRoll, Toast.LENGTH_SHORT).show();
        databaseReference.child(uRoll).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataholder userProf= snapshot.getValue(dataholder.class);
                if(userProf!=null)
                {
                    String Name= userProf.Name;
                    String Roll= userProf.RollNo;
                    String Pfp= userProf.Pfp;
//                    Toast.makeText(HomePage.this, Pfp, Toast.LENGTH_SHORT).show();
                    name.setText(Name);
                    roll.setText(Roll);
                    imgpath= FirebaseStorage.getInstance().getReferenceFromUrl(Pfp);
                    Glide.with(dp).load(Pfp).into(dp);


                }
                else{
                    Toast.makeText(HomePage.this, "Data Retrieve Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomePage.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.homeLogout:
                logOut();
                break;
            case R.id.lostFeed:
                startActivity(new Intent(HomePage.this, LostFeed.class));
                break;
            case R.id.foundFeed:
                startActivity(new Intent(HomePage.this, FoundFeed.class));
                break;
            case R.id.lostItem:
                startActivity(new Intent(HomePage.this, ReportLost.class));
                break;
            case R.id.foundItem:
                startActivity(new Intent(HomePage.this, ReportFound.class));
                break;
            case R.id.myPosts:
                startActivity(new Intent(HomePage.this, MyPosts.class));
                break;
            case R.id.updatePass:
                startActivity(new Intent(HomePage.this, UpdatePass.class));
                break;
        }
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        user=mAuth.getCurrentUser();

        if(user== null)
        {
            startActivity(new Intent(HomePage.this, MainActivity.class));
        }
        else
        {
            Toast.makeText(HomePage.this, "Log out Failed", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user= mAuth.getCurrentUser();
        if(user== null){
            startActivity(new Intent(HomePage.this, MainActivity.class));
        }

    }
}