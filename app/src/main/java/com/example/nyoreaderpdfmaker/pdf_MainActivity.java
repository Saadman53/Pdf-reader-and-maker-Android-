package com.example.nyoreaderpdfmaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.util.ArrayList;


public class pdf_MainActivity extends AppCompatActivity {

    ListView listView;

    public static ArrayList<File> fileList= new ArrayList<>();

    pdf_adapter obj_adapter;
    public  static  int REQUEST_PERMISSION=1;

    boolean boolean_permission;

    File dir;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        setTitle("PDF LIST");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_activity_main);

        listView=findViewById(R.id.listViewPDF);

        dir=new File(Environment.getExternalStorageDirectory().toString());

        permission_fn();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(getApplicationContext(), view_pdf_files.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
    }

    private void permission_fn() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(pdf_MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))
            {

            }
            else{
                ActivityCompat.requestPermissions(pdf_MainActivity.this,new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                },REQUEST_PERMISSION);
            }

        }
        else
        {
            boolean_permission=true;
            getFile(dir);
            obj_adapter=new pdf_adapter(getApplicationContext(),fileList);
            listView.setAdapter(obj_adapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==REQUEST_PERMISSION){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                boolean_permission=true;
                getFile(dir);
                obj_adapter=new pdf_adapter(getApplicationContext(),fileList);
                listView.setAdapter(obj_adapter);

            }

            else{
                Toast.makeText(this,"Please Allow Permission",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public  ArrayList<File> getFile(File dir){
        File listFile[]=dir.listFiles();

        if(listFile!=null && listFile.length>0){
            for(int i=0;i<listFile.length;i++){
                if(listFile[i].isDirectory()){
                    getFile(listFile[i]);
                }
                else{
                    boolean boolean_pdf=false;
                    if(listFile[i].getName().endsWith(".pdf")){
                        for(int j=0;j<fileList.size();j++){
                            if(fileList.get(j).getName().equals(listFile[i].getName())){
                                boolean_pdf=true;
                            }
                            else{

                            }
                        }

                        if(boolean_pdf){
                            boolean_pdf=false;
                        }
                        else
                        {
                            fileList.add(listFile[i]);
                        }
                    }

                }
            }
        }

        return fileList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.search_bar,menu);

        MenuItem menuItem = menu.findItem(R.id.searchViewID);
        SearchView searchView=(SearchView)menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                obj_adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


}
