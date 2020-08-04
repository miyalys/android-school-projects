package com.example.quarantineapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Covid19Activity extends AppCompatActivity {
    TextView testedTxt;
    TextView infectedTxt;
    TextView recoveredTxt;
    TextView deceasedTxt;
    ImageButton refreshBtn;

    RequestQueue queue; //Queue, to be used to queue the request
    //API URL
    String url = "https://api.apify.com/v2/key-value-stores/EAlpwScH29Qa5m60g/records/LATEST?disableRedirect=true";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_statistics);


        testedTxt = findViewById(R.id.tested_api);
        infectedTxt = findViewById(R.id.infected_api);
        recoveredTxt = findViewById(R.id.recovered_api);
        deceasedTxt = findViewById(R.id.deceased_api);
        refreshBtn = findViewById(R.id.refreshBtn);

        callStatisticApi();
    }

    public void refreshBtnPressed (View view){
        callStatisticApi();
    }


    private void callStatisticApi() {
        queue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            testedTxt.setText(String.valueOf(jsonObject.getInt("tested")));
                            infectedTxt.setText(String.valueOf(jsonObject.getInt("infected")));
                            recoveredTxt.setText(String.valueOf(jsonObject.getInt("recovered")));
                            deceasedTxt.setText(String.valueOf(jsonObject.getInt("deceased")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        queue.add(stringRequest);
    }


}
