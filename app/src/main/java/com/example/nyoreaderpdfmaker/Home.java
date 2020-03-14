package com.example.nyoreaderpdfmaker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.*;

import android.content.ContentResolver;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import static com.example.nyoreaderpdfmaker.R.id.aboutLinear;
import static com.example.nyoreaderpdfmaker.R.id.cameraID;
import static com.example.nyoreaderpdfmaker.R.id.dpImageID;
import static com.example.nyoreaderpdfmaker.R.id.header_user_ID;
import static com.example.nyoreaderpdfmaker.R.id.top;
import static com.example.nyoreaderpdfmaker.R.id.up;
import static com.example.nyoreaderpdfmaker.R.layout.fragment_about_developer;

public class Home extends AppCompatActivity{



    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;

    NavigationView navigationView;

    TextView username,userEmail;

    final String Tag = Home.class.getSimpleName();

     private LinkedList<User> userlist;


     private TextView textView;






    TextView aboutText;

    public static String res;




    DatabaseReference databaseReference,uploadDatabaseReference;

    ImageView dpImage,camera;

    private Uri ImageUri;

    private static final int IMAGE_REQUEST=1;

    StorageReference storageReference;

    FirebaseUser firebaseUser;


    ///pdf





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        setTitle("Home");

        textView=findViewById(R.id.home_text);

        uploadDatabaseReference=FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference("Upload");



        drawerLayout= findViewById(R.id.drawer);

        mToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.Open,R.string.Close);

        drawerLayout.addDrawerListener(mToggle);

        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.navigation_view);

        final View header= navigationView.getHeaderView(0);

        dpImage=header.findViewById(dpImageID);
        camera=header.findViewById(cameraID);
        username = header.findViewById(header_user_ID);
        userEmail = header.findViewById(R.id.header_user_email);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Choose a file to upload as dp",Toast.LENGTH_SHORT).show();

                openFileChooser();


            }
        });


         firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        String email="";

        if(firebaseUser !=null) {
            email = firebaseUser.getEmail();

            //userEmail.setText(email);
        }


        databaseReference=FirebaseDatabase.getInstance().getReference();



        userlist= new LinkedList<>();





        uploadDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    if(!dataSnapshot1.getKey().equals("users")){
                       Upload upload= dataSnapshot1.getValue(Upload.class);

                       if(dataSnapshot1.getKey().equals(firebaseUser.getUid())) {

                            Picasso.with(getApplicationContext()).load(upload.getImageUri()).transform(new CircleTransform()).into(dpImage);

                           //Toast.makeText(getApplicationContext(),upload.getImageUri(),Toast.LENGTH_LONG).show();
                       }


                       }

                   ///Toast.makeText(getApplicationContext(),dataSnapshot1.getKey(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userlist.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String firstname=dataSnapshot1.child("firstname").getValue().toString();
                    String secondname=dataSnapshot1.child("secondname").getValue().toString();
                    String Eemail=dataSnapshot1.child("email").getValue().toString();
                    userlist.add(new User(Eemail,firstname,secondname));

                    if (Eemail.equals(firebaseUser.getEmail())){
                        String name = firstname + " " + secondname;
                        username.setText(name);
                        userEmail.setText(Eemail);
                        textView.setText("Welcome "+firstname);
                    }
            }


        }
        @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });











        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId()==R.id.logoutID){

                    FirebaseAuth.getInstance().signOut();

                    Toast.makeText(Home.this,"Successfully logged out",Toast.LENGTH_LONG).show();
                    finish();



                    startActivity(new Intent(Home.this,MainActivity.class));

                    item.setChecked(true);


                    //log out
                }
                else if(item.getItemId()==R.id.aboutDev)
                {
                    //json parse
                    Intent intent = new Intent(getApplicationContext(),aboutDeveloper.class);
                    startActivity(intent);

                    item.setChecked(true);
                }

                else if(item.getItemId()==R.id.pdflists)
                {
                    startActivity(new Intent(getApplicationContext(),pdf_MainActivity.class));
                    item.setChecked(true);
                }

                else if(item.getItemId()==R.id.createID){
                    startActivity(new Intent(getApplicationContext(),CreatePDF.class));
                    item.setChecked(true);
                }

                return false;
            }
        });
    }







    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       if(mToggle.onOptionsItemSelected(item)){
           return true;
       }



       return super.onOptionsItemSelected(item);
    }

    public  void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            ImageUri=data.getData();
            Picasso.with(this).load(ImageUri).transform(new CircleTransform()).into(dpImage);
            saveData();
        }

    }

    public String getFileExtension(Uri imageUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }


    public void saveData(){
        StorageReference ref = storageReference.child(firebaseUser.getEmail()+"."+getFileExtension(ImageUri));


        ref.putFile(ImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content

                        Toast.makeText(getApplicationContext(),"Picture successfully uploaded",Toast.LENGTH_SHORT).show();

                        ///before uploading lets delete the previous references

                        //deletedata();




                        ///

                        DatabaseReference dref= FirebaseDatabase.getInstance().getReference();

                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();

                        while(!uriTask.isSuccessful());

                        Uri downloadURl=uriTask.getResult();
                        Upload upload=new Upload(firebaseUser.getEmail(),downloadURl.toString());

                        //String uploadID=dref.push().getKey();

                        String uploadID = firebaseUser.getUid();


                        dref.child(uploadID).setValue(upload);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });


    }

    private void deletedata() {
        DatabaseReference nref = FirebaseDatabase.getInstance().getReference();


        Query applesQuery = nref.orderByChild("imageName").equalTo(firebaseUser.getEmail());

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Error: "+databaseError.toException(),Toast.LENGTH_SHORT).show();
            }
        });
    }




}
