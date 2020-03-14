package com.example.nyoreaderpdfmaker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class aboutDeveloper extends AppCompatActivity {

    public TextView textView;
    RequestQueue requestQueue;

    String res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_developer);
        textView=findViewById(R.id.aboutmeTextID);

        setTitle("About Developer");

        //gson Gson= new gson(this);
        requestQueue= Volley.newRequestQueue(this);


        String url="https://api.myjson.com/bins/anh7c";




        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONObject admin=response.getJSONObject("admin");//jsonArray.getJSONObject(0);
                            String name=admin.getString("Name");
                            int age=admin.getInt("Age");
                            String contact=admin.getString("Contact me");
                            String about=admin.getString("About me");

                            res="Name: "+name+"\n"+"Age: "+age+"\n"+"Contact me on "+contact+"\n"+about+"\n";

                            //Toast.makeText(getApplicationContext(),"Name: "+name+"\n"+"Age: "+age+"\n"+"Contact me on "+contact+"\n"+"About me: "+about+"\n",Toast.LENGTH_LONG).show();
                            textView.setText(res);

                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);



        //Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();



    }
}
