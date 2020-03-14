package com.example.nyoreaderpdfmaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {



    EditText password,Email;

    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("LOG IN");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null){
            finish();


            Intent intent = new Intent(MainActivity.this, Home.class);
            startActivity(intent);
        }

        password= findViewById(R.id.PassowrdID);

        password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        Button signup = findViewById(R.id.signupID);
        Button login= findViewById(R.id.loginID);
        Button forgot= findViewById(R.id.forgotButtonID);

        signup.setOnClickListener(this);
        login.setOnClickListener(this);
        forgot.setOnClickListener(this);

        Email=findViewById(R.id.UsernameID);

        progressBar=findViewById(R.id.progressID);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.signupID)
        {
            Intent intent = new Intent(MainActivity.this,SignUp_activity.class);
            startActivity(intent);
        }
        if(v.getId()==R.id.loginID){
            login();
        }

        if(v.getId()==R.id.forgotButtonID){
            startActivity(new Intent(MainActivity.this,forgot_activity.class));
        }
    }

    private void login() {
        String email=Email.getText().toString();

        String passText=password.getText().toString();

        if(email.equals(""))
        {
            Email.setError("Enter an email address");
            Email.requestFocus();
        }

        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Email.setError("Enter a valid email address");
            Email.requestFocus();
        }

        //checking the validity of the password
        else if(passText.equals(""))
        {
            password.setError("Enter a valid password");
            password.requestFocus();

        }

        else if(passText.length()<6){
            password.setError("Minimum length of password is 6");
            password.requestFocus();
        }
        else if(passText.length()>10){
            password.setError("Maximum length of password is 10");
            password.requestFocus();
        }

        else {

            progressBar.setVisibility(View.VISIBLE);


            mAuth.signInWithEmailAndPassword(email, passText).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        if (mAuth != null) {

                            boolean x = false;

                            try {
                                if (mAuth.getCurrentUser().isEmailVerified()) x = true;
                                else x = false;
                            } catch (NullPointerException e) {

                            }

                            if (x) {
                                Toast.makeText(getApplicationContext(), "Log In is successfull", Toast.LENGTH_LONG).show();

                                finish();


                                Intent intent = new Intent(MainActivity.this,Home.class);
                                startActivity(intent);
                                
                            } else {
                                Toast.makeText(getApplicationContext(), "Please verify your email address", Toast.LENGTH_LONG).show();
                            }
                        }
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

}
