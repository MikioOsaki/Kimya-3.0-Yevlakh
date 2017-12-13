    package com.example.mikio.kimya_3_0_Yevlakh;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

    public class FormActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Button buttonBack = (Button) findViewById(R.id.buttonBack);
        Button buttonSave = (Button) findViewById(R.id.buttonSpeichern);
        buttonBack.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
    }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.buttonBack:
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                    finish();
                    break;
                case R.id.buttonSpeichern:


                    // Instantiate the RequestQueue.
                    RequestQueue queue = Volley.newRequestQueue(this);
                    String url = "http://192.168.1.100:8001/kimya_test/insert.php";
                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.
                                    Toast.makeText(FormActivity.this, "Response is: "+response, Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(FormActivity.this, "That didn't work!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);

                    Toast.makeText(this, "Anwendung wurde erfolgreich gespeichert.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
