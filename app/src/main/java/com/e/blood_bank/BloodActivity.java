package com.e.blood_bank;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.blood_bank.Adapter.RequestAdapter;
import com.e.blood_bank.DataModela.RequestDataModel;
import com.e.blood_bank.Utils.Endpoints;
import com.e.blood_bank.Utils.VolleySingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class BloodActivity extends AppCompatActivity {

    private Toolbar top;
    private BottomNavigationView bottom;
    private RecyclerView recyclerView;
    private List<RequestDataModel> requestDataModels;
    private RequestAdapter requestAdapter;
    private Button button;
    private FloatingActionButton fabMic;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private String keeper="";

    //Shake listener

    private SensorManager sensorManager;

    private float acelVal;
    private float acelLast;
    private float shake;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood);

        //Shake Listeners
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);

        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;

        //Setting up speech recognizer intent

       check();
        speechRecognizer= speechRecognizer.createSpeechRecognizer(BloodActivity.this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        //Setting up Speech recognizer

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results)
            {

                ArrayList<String> matchesFound = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if(matchesFound != null)
                {

                    keeper = matchesFound.get(0);

                        if(keeper.equals("blood"))
                        {

                            Toast.makeText(BloodActivity.this, "Donate blood...!! ( " + keeper+" )", Toast.LENGTH_LONG).show();

                        }
                        else if(keeper.equals("plasma"))
                        {
                            Toast.makeText(BloodActivity.this, "Donate Plasma...!! ( " + keeper+" )", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(BloodActivity.this,PlasmaActivity.class);
                            startActivity(intent);


                        }
                        else if((keeper.equals("request blood"))||keeper.equals("blood request"))
                        {

                            Toast.makeText(BloodActivity.this, "Helping you to find donors...!! ( " + keeper+" )", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(BloodActivity.this,Request_Blood.class);
                            startActivity(intent);

                        }
                        else if((keeper.equals("request plasma"))||keeper.equals("plasma request"))
                        {

                            Toast.makeText(BloodActivity.this, "Helping you to find donors...!! ( " + keeper+" )", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(BloodActivity.this,RequestPlasma.class);
                            startActivity(intent);


                        }
                        else if(keeper.equals("tracker"))
                        {

                            Toast.makeText(BloodActivity.this, "Fetching a glimpse of covid cases ...!! ( " + keeper+" )", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(BloodActivity.this, TrackerActivity.class);
                            startActivity(intent);


                        }
                        else if((keeper.equals("search plasma"))||keeper.equals("plasma search"))
                        {

                            Toast.makeText(BloodActivity.this, "Searching plasma donors..!! ( " + keeper+" )", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(BloodActivity.this,PlasmaSearch.class);
                            startActivity(intent);


                        }
                        else if((keeper.equals("search blood"))||keeper.equals("blood search"))                        {

                            Toast.makeText(BloodActivity.this, "Searching blood donors..!! ( " + keeper+" )", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(BloodActivity.this,SearchActivity.class);
                            startActivity(intent);


                        }
                        else if((keeper.equals("log out"))||keeper.equals("bahar chlo"))
                        {

                            Toast.makeText(BloodActivity.this, "Thank you for helping others ...!! ( " + keeper+" )", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(BloodActivity.this,WelcomeScreen.class);
                            startActivity(intent);


                        }
                        else
                        {
                            Toast.makeText(BloodActivity.this, "Command:-  " + keeper +" (Does not exist)  ", Toast.LENGTH_LONG).show();
                        }


                }





            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });





        //initialize

        top = findViewById(R.id.topCont);
        bottom = findViewById(R.id.bottombar);
        recyclerView = findViewById(R.id.Recycle);
        requestDataModels = new ArrayList<>();
        requestAdapter = new RequestAdapter(requestDataModels,this);
        button = findViewById(R.id.Request);
        fabMic = findViewById(R.id.fabMic);




        //On CLick

//        // Fab
//        fabMic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MainActivity.this,"Speak Up...!!!",Toast.LENGTH_SHORT).show();
//                speechRecognizer.startListening(speechRecognizerIntent);
//                keeper = "";
//            }
//        });


        fabMic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {

                    case MotionEvent.ACTION_DOWN:
                        speechRecognizer.startListening(speechRecognizerIntent);
                        keeper = "";
                        break;
                    case MotionEvent.ACTION_UP:
                        speechRecognizer.stopListening();
                        break;

                }

                return false;
            }
        });




        top.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.logout) {
                    Intent intent1 = new Intent(BloodActivity.this, WelcomeScreen.class);
                    startActivity(intent1);
                    BloodActivity.this.finish();
                    Toast.makeText(BloodActivity.this, "LOGGING OUT...", Toast.LENGTH_SHORT).show();

                } else if (item.getItemId() == R.id.Settings) {
                    Intent intent2 = new Intent(BloodActivity.this, SettingActivity.class);
                    startActivity(intent2);
                    Toast.makeText(BloodActivity.this, "SOON IN ACTION...", Toast.LENGTH_SHORT).show();

                } else if(item.getItemId()== R.id.search) {
                    Intent intent3 = new Intent(BloodActivity.this, SearchActivity.class);
                    startActivity(intent3);

                }
                return false;
            }
        });




        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.plasma)
                {

                    Intent intent = new Intent(BloodActivity.this,PlasmaActivity.class);
                    startActivity(intent);
                    BloodActivity.this.finish();
                }
                else if(menuItem.getItemId()==R.id.map)
                {

                    Intent intent = new Intent(BloodActivity.this, TrackerActivity.class);
                    startActivity(intent);

                }
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BloodActivity.this,Request_Blood.class);
                startActivity(intent);
            }
        });

        //Recycler view

        LinearLayoutManager layoutManager= new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(requestAdapter);
        populateHomePage();
        TextView pick_location = findViewById(R.id.Location);
        String location = PreferenceManager.getDefaultSharedPreferences(this).getString("city","No_city_found");

        if(!location.equals("No_city_found"))
        {
            pick_location.setText(location);

        }


    }
    // Speech to text recogination
//

    private void populateHomePage()
    {
        final String city =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("city","no_city");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.getreq, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Gson gson= new Gson();
                Type type = new TypeToken<List<RequestDataModel>>(){}.getType();
                List<RequestDataModel> dataModels = gson.fromJson(response,type);

                requestDataModels.addAll(dataModels);
                requestAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BloodActivity.this, "OOps something went wrong :(", Toast.LENGTH_SHORT).show();
                Log.d("Volley", Objects.requireNonNull(error.getMessage()));
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
               // params.put("city",city);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }


      private void check()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
        {
            if(!(ContextCompat.checkSelfPermission(BloodActivity.this, Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED))
            {
                Intent intent;
                intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+getPackageName()));
                startActivity(intent);
                finish();

            }
        }

    }

    //Shake listener
    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            acelLast = acelVal;

            acelVal = (float) Math.sqrt((double) (x*x + y*y + z*z));

            float delta = acelVal - acelLast;

            shake = shake * 0.9f + delta;

            if (shake > 12)
            {
                Intent shakeChilly =new Intent(BloodActivity.this, TrackerActivity.class);
                startActivity(shakeChilly);
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

}
