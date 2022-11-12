package com.example.lostandfound;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.util.Random;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{

    boolean dpflag= false;

    ImageView pfp;
    TextView login;

    EditText name, email, roll, passW, phone, wp, cpassW;
    Uri filepath;
    ImageView img ;
    Button register;
    Bitmap bitmap;
    ProgressBar pb;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth= FirebaseAuth.getInstance();
        login= (TextView) findViewById(R.id.regLogin);
        login.setOnClickListener(this);
        pfp= findViewById(R.id.regDP);
        pfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePic();

            }


            private void choosePic() {
                Dexter.withActivity(RegisterUser.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent= new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, "Select Image File"), 1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                Toast.makeText(getApplicationContext(), "Please Provide Permission", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();

            }


        });

        img= (ImageView) findViewById((R.id.regDP));
        register= (Button) findViewById((R.id.regSubmit));

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        img= (ImageView) findViewById((R.id.regDP));
        name=(EditText) findViewById(R.id.regName);
        email=(EditText) findViewById(R.id.regEmail);
        roll=(EditText) findViewById(R.id.regRoll);
        passW=(EditText) findViewById(R.id.regPW);
        phone=(EditText) findViewById(R.id.regPhone);
        wp=(EditText) findViewById(R.id.regWP);
        cpassW=(EditText) findViewById(R.id.regConfirm);
        pb= (ProgressBar) findViewById(R.id.regPB);

        String editname = name.getText().toString().trim();
        String editroll = roll.getText().toString().trim();
        String editemail = email.getText().toString().trim();
        String pw1 = passW.getText().toString();
        String pw2 = cpassW.getText().toString();
        String editphone = phone.getText().toString();
        String editWP = wp.getText().toString();


        if (editname.isEmpty()) {
            name.setError("Name is required!");
            name.requestFocus();
            return;
        }
        if (editroll.isEmpty()) {
            roll.setError("Roll No. is required!");
            roll.requestFocus();
            return;
        }
        if (editemail.isEmpty()) {
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }
        if (pw1.isEmpty()) {
            passW.setError("Please create a Password");
            passW.requestFocus();
            return;
        }
        if (pw2.isEmpty()) {
            if (pw1.isEmpty()) {
                passW.setError("Please create a Password");
                passW.requestFocus();
                return;
            }
            cpassW.setError("Passwords do not match");
            passW.requestFocus();
            cpassW.requestFocus();
            return;
        }
        if (editphone.isEmpty()) {
            phone.setError("Phone number is required!");
            phone.requestFocus();
            return;
        }
        if (editWP.isEmpty()) {
            wp.setError("Whatsapp Number is required!");
            wp.requestFocus();
            return;
        }
        int i, flag = 1;
        //checking roll number

        char c;
        if (editroll.length() == 8) {
            for (i = 0; i < 4; i++) {
                c = editroll.charAt(i);
                if (c >= '0' && c <= '9') ;
                else {
                    flag = 0;
                    break;
                }
            }
            if (!(Character.isLetter(editroll.charAt(4)) && Character.isLetter(editroll.charAt(5)) && Character.isDigit(editroll.charAt(6)) && Character.isDigit(editroll.charAt(7)))) {
                flag = 0;
            }
        } else {
            flag = 0;
        }
        if (flag == 0) {
            roll.setError("Invalid Roll Number!");
            roll.requestFocus();
            return;
        }

        //checking email
        if (!editemail.endsWith("@iitp.ac.in") || editemail.startsWith("@iitp.ac.in")) {
            email.setError("Use Institute Email ID only");
            email.requestFocus();
            return;
        }
        if(!editemail.contains(editroll)){
            email.setError("Email and roll number do not belong to same person");
            email.requestFocus();
            return;
        }

        //checking password
        if (pw1.length() < 8) {
            passW.setError("Password should have a minimum of eight characters!");
            passW.requestFocus();
            return;
        }
        if (!pw1.equals(pw2)) {
            cpassW.setError("Passwords do not match");
            passW.requestFocus();
            cpassW.requestFocus();
            return;
        }

        //checking phone numbers
        if (editphone.length() != 10)
        {
            phone.setError("Please enter valid 10-digit Phone Number");
            phone.requestFocus();
            return;
        }
        if (editWP.length() != 10)
        {
            wp.setError("Please enter valid 10-digit Phone Number");
            wp.requestFocus();
            return;
        }
        if(!dpflag)
        {
            Toast.makeText(getApplicationContext(), "Upload Profile Picture", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(editemail, pw1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            uploadtoFB();
                        }
                        else{
                            Toast.makeText(RegisterUser.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void uploadtoFB() {


        name=(EditText) findViewById(R.id.regName);
        email=(EditText) findViewById(R.id.regEmail);
        roll=(EditText) findViewById(R.id.regRoll);
        passW=(EditText) findViewById(R.id.regPW);
        phone=(EditText) findViewById(R.id.regPhone);
        wp=(EditText) findViewById(R.id.regWP);
        cpassW=(EditText) findViewById(R.id.regConfirm);
        pb= (ProgressBar) findViewById(R.id.regPB);
        user= FirebaseAuth.getInstance().getCurrentUser();



        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
        StorageReference uploader= firebaseStorage.getReference("ProfilePicture/Image1"+new Random().nextInt(50));
        uploader.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                FirebaseDatabase db= FirebaseDatabase.getInstance();
                                DatabaseReference root= db.getReference("Users");
                                dataholder obj= new dataholder( email.getText().toString(), name.getText().toString(), passW.getText().toString(), uri.toString(), phone.getText().toString(), roll.getText().toString(), wp.getText().toString());
                                root.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(obj);
                                name.setText("");
                                email.setText("");
                                roll.setText("");
                                passW.setText("");
                                phone.setText("");
                                cpassW.setText("");
                                wp.setText("");
                                img.setImageResource(R.drawable.pngfind_com_upload_icon_png_661092);
                                Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_LONG).show();
                                pb.setVisibility(View.GONE);


                                    user.sendEmailVerification();
                                    Toast.makeText(RegisterUser.this, "Verification Link Sent to Email", Toast.LENGTH_SHORT).show();
                                    mAuth.signOut();


                                startActivity(new Intent(RegisterUser.this, MainActivity.class));
                            }
                        });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        pb.setVisibility(View.VISIBLE);
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.regLogin:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK)
        {
            dpflag=true;
            filepath= data.getData();
            try{
                InputStream inputStream= getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
            }
            catch(Exception ex){

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}