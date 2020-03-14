package com.example.nyoreaderpdfmaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp_activity extends AppCompatActivity implements View.OnClickListener {

    EditText Email,password,confirm,first_name,second_name;
    Button button;

    private FirebaseAuth mAuth,firebaseAuth;

    DatabaseReference databaseReference;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Sign UP");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_activity);

        mAuth = FirebaseAuth.getInstance();

        Email=findViewById(R.id.email_nameEDIT);
        password=findViewById(R.id.passwordEDIT);
        confirm=findViewById(R.id.confirmEDIT);
        first_name=findViewById(R.id.first_nameEDIT);
        second_name=findViewById(R.id.second_nameEDIT);

        button=findViewById(R.id.signup_button2);

        button.setOnClickListener(this);

        progressBar=findViewById(R.id.progress_bar);

        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        confirm.setTransformationMethod(PasswordTransformationMethod.getInstance());



    }

    @Override
    public void onClick(View v) {
        final String email = Email.getText().toString();

        String passText = password.getText().toString();

        if (first_name.getText().toString().isEmpty()) {
            first_name.setError("Field Empty!!");
            first_name.requestFocus();
        } else if (second_name.getText().toString().isEmpty()) {
            second_name.setError("Field Empty!!");
            second_name.requestFocus();
        } else if (email.isEmpty()) {
            Email.setError("Enter an email address");
            Email.requestFocus();
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.setError("Enter a valid email address");
            Email.requestFocus();
        }

        //checking the validity of the password
        else if (passText.isEmpty()) {
            password.setError("Enter a valid password");
            password.requestFocus();

        } else if (!passText.equals(confirm.getText().toString())) {
            confirm.setError("Passwords don't match!!");
            confirm.requestFocus();
        } else if (passText.length() < 6) {
            password.setError("Minimum length of password is 6");
            password.requestFocus();
        } else if (passText.length() > 10) {
            password.setError("Maximum length of password is 10");
            password.requestFocus();
        } else {

            progressBar.setVisibility(View.VISIBLE);



            mAuth.createUserWithEmailAndPassword(email, passText).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

//                public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
//                    FirebaseUser user = firebaseAuth.getCurrentUser();
//                    if (user != null) {
//                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
//
//                        String key= databaseReference.push().getKey();
//                        User use= new User(Email.getText().toString(),first_name.getText().toString(),second_name.getText().toString());
//
//                        ref.child("users").child(user.getUid()).setValue(use);
//                    }
//                }

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);


                    if (task.isSuccessful()) {
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {


                                    database_read();
                                    Toast.makeText(SignUp_activity.this, "Registered Successful, Please check your email for verification", Toast.LENGTH_LONG).show();
                                    Task usertask = mAuth.getCurrentUser().reload();
//                                usertask.addOnSuccessListener(new OnSuccessListener() {
//                                    @Override
//                                    public void onSuccess(Object o) {
//                                        FirebaseUser user = mAuth.getCurrentUser();
//                                        boolean useremailveri = user.isEmailVerified();
//                                        String useremailuid = user.getUid();
//                                    }
//
//
//                                });
                                    finish();
                                } else {

                                    Toast.makeText(SignUp_activity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }


                        });
                        //Toast.makeText(getApplicationContext(), "Sign Up is successfull", Toast.LENGTH_SHORT).show();
                        //buildActionCodeSettings();

                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getApplicationContext(), "User is already Registered", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            });
        }
    }

    public void database_read(){

        databaseReference= FirebaseDatabase.getInstance().getReference("users");

        User user= new User(Email.getText().toString(),first_name.getText().toString(),second_name.getText().toString());


        String key=databaseReference.push().getKey();

        databaseReference.child(key).setValue(user);

    }






}
