package com.example.nyoreaderpdfmaker;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class aboutDeveloperFragment extends Fragment {

    public TextView textView;
    RequestQueue requestQueue;

    String res;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        View customview= inflater.inflate(R.layout.fragment_about_developer, container, false);

        textView=customview.findViewById(R.id.aboutmeTextID);



        Toast.makeText(getContext(),res,Toast.LENGTH_LONG).show();

        textView.setText(res);

        return customview;

    }

    public void SetText(String text){
        textView.setText(text);
    }


}
