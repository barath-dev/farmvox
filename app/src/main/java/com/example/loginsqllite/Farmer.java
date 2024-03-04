package com.example.loginsqllite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class Farmer extends AppCompatActivity {
    TextView question;
    TextView answer;
    Button btnSpeak;
    Button btnSellCrops, btnMyCrops;
    TextToSpeech textToSpeech;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer);
        String username = getIntent().getStringExtra("username");
        question = findViewById(R.id.question);
        answer = findViewById(R.id.answer);
        btnSpeak = findViewById(R.id.submit);
        btnSellCrops = findViewById(R.id.sellCrops);
        btnMyCrops = findViewById(R.id.MyProducts);
        TextView textView = findViewById(R.id.textView);
        textView.setText("FarmVox");
        TextView tag = findViewById(R.id.tagline);
        tag.setText("Vibrant Farms,Voice as Nature's Arms");

        btnMyCrops.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Start the "MyCrops" activity
                Intent intent = new Intent(Farmer.this, MyCrops.class);
                intent.putExtra("username",username);// Replace "MyCrops" with the actual activity representing the "MyCrops" page
                startActivity(intent);
            }
        });
        btnSellCrops.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Start the "Sell" activity
                Intent intent = new Intent(Farmer.this, Sellproduct.class);
                intent.putExtra("username",username);// Replace "Sell" with the actual activity representing the "sell" page
                startActivity(intent);
            }
        });
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SpeakNow(view);
            }
        });
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
    }

    private void SpeakNow(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "te-IN");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start Speaking...");
        int requestCode = 111;
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 111 && resultCode == RESULT_OK) {
            try {
                String queryText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
                question.setText(queryText);

                // Make the HTTP request and get the response
                makeHttpRequest(queryText);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void makeHttpRequest(String queryText) {
        try {
            // Encode the query text
            String query_Text = URLEncoder.encode(queryText, "UTF-8");
            // Construct the URL with the encoded query text
            String url = "http://192.168.29.228:5000/predict?query_text="+query_Text;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Update the 'answer' TextView with the response
                            try {
                                // Parse the JSON response
                                JSONObject jsonResponse = new JSONObject(response);
                                // Extract the "response" field from the JSON object
                                String result = jsonResponse.getString("response");
                                // Update the 'answer' TextView with the result
                                if (result.equals("-1")) {
                                    // If result is -1, start the "sell" activity
                                    Intent intent = new Intent(Farmer.this, Sellproduct.class); // Replace SellActivity with the actual activity representing the "sell" page
                                    startActivity(intent);
                                } else {
                                    // Update the 'answer' TextView with the result
                                    answer.setText(result);
                                    textToSpeech.speak(result, TextToSpeech.QUEUE_FLUSH, null);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                String errorMessage = "Failed to parse JSON response: " + e.getMessage();
                                answer.setText(errorMessage);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle the error or display an error message
                            String errorMessage = "Connection Failed. Error: " + error.getMessage();
                            answer.setText(errorMessage);
                        }
                    });

            // Add the request to the RequestQueue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
