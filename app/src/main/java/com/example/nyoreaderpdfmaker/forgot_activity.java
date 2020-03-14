package com.example.nyoreaderpdfmaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgot_activity extends AppCompatActivity {

    Button forgot_button;
    EditText Email;

    ProgressBar progressBar;

    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_activity);
        setTitle("Forgot password");

        forgot_button=findViewById(R.id.reset_buttonID);

        Email=findViewById(R.id.forgotEmailID);

        progressBar=findViewById(R.id.progress_forgot_ID);

        firebaseAuth=FirebaseAuth.getInstance();

        forgot_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email= Email.getText().toString();

                progressBar.setVisibility(View.VISIBLE);

                if(email.equals("")){
                    Email.setError("Enter an email address");
                    Email.requestFocus();
                }
                else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    Email.setError("Enter a valid email address");
                    Email.requestFocus();
                }
                else{
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Reset link has been send to your email",Toast.LENGTH_LONG).show();

                                finish();

                                startActivity(new Intent(forgot_activity.this,MainActivity.class));
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Error : "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });


    }
}
