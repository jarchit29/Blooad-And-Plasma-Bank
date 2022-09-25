package com.e.blood_bank;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.blood_bank.Adapter.PRequestAdapter;
import com.e.blood_bank.DataModela.PRequestDataModel;
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

public class PlasmaActivity extends AppCompatActivity {

    private Toolbar ptop;
    private BottomNavigationView pbottom;
    private RecyclerView recyclerView;
    private ArrayList<PRequestDataModel> requestDataModels;
    private PRequestAdapter requestAdapter;
    private FloatingActionButton fabMic;
    private Button request;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private String keeper="";

//    private FloatingActionButton fabHin,fabEng; was done to extend mic options
//    Animation
//    Animation fabOpen,fabClose,fabRc,fabRa;
//    boolean isOpen = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plasma);


        //Setting up speech recognizer intent
        
        speechRecognizer= speechRecognizer.createSpeechRecognizer(PlasmaActivity.this);
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

                        Toast.makeText(PlasmaActivity.this, "Donate blood...!! ( " + keeper+" )", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(PlasmaActivity.this, BloodActivity.class);
                        startActivity(intent);

                    }
                    else if(keeper.equals("plasma"))
                    {
                        Toast.makeText(PlasmaActivity.this, "Donate Plasma...!! ( " + keeper+" )", Toast.LENGTH_LONG).show();



                    }
                    else if((keeper.equals("request blood"))||keeper.equals("blood request"))
                    {

                        Toast.makeText(PlasmaActivity.this, "Helping you to find donors...!! ( " + keeper+" )", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(PlasmaActivity.this,Request_Blood.class);
                        startActivity(intent);

                    }
                    else if((keeper.equals("request plasma"))||keeper.equals("plasma request"))
                    {

                        Toast.makeText(PlasmaActivity.this, "Helping you to find donors...!! ( " + keeper+" )", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(PlasmaActivity.this,RequestPlasma.class);
                        startActivity(intent);


                    }
                    else if(keeper.equals("tracker"))
                    {

                        Toast.makeText(PlasmaActivity.this, "Fetching a glimpse of covid cases ...!! ( " + keeper+" )", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(PlasmaActivity.this, TrackerActivity.class);
                        startActivity(intent);


                    }
                    else if((keeper.equals("search plasma"))||keeper.equals("plasma search"))
                    {

                        Toast.makeText(PlasmaActivity.this, "Searching plasma donors..!! ( " + keeper+" )", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(PlasmaActivity.this,PlasmaSearch.class);
                        startActivity(intent);


                    }
                    else if((keeper.equals("search blood"))||keeper.equals("blood search"))                        {

                        Toast.makeText(PlasmaActivity.this, "Searching blood donors..!! ( " + keeper+" )", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(PlasmaActivity.this,SearchActivity.class);
                        startActivity(intent);


                    }
                    else if((keeper.equals("log out"))||keeper.equals("bahar chlo"))
                    {

                        Toast.makeText(PlasmaActivity.this, "Thank you for helping others ...!! ( " + keeper+" )", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(PlasmaActivity.this,WelcomeScreen.class);
                        startActivity(intent);


                    }
                    else
                    {
                        Toast.makeText(PlasmaActivity.this, "Command:-  " + keeper +" (Does not exist)  ", Toast.LENGTH_LONG).show();
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




        //Initialize

        ptop = findViewById(R.id.ptopCont);
        pbottom = findViewById(R.id.pbottombar);
        fabMic = findViewById(R.id.PfabMic);
        request = findViewById(R.id.pRequest);
        recyclerView = findViewById(R.id.pRecycle);
        requestDataModels = new ArrayList<PRequestDataModel>();
        requestAdapter = new PRequestAdapter(requestDataModels,this);

        //   fabHin = findViewById(R.id.PfabHin);
        //   fabEng = findViewById(R.id.PfabEng);

//        //Animations
//
//        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.from_bottom_anim);
//        fabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.from_close_anim);
//        fabRc = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_open_anim);
//        fabRa = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_close_anim);



        //On CLick for fab buttons
//
//        // Fab
//        fabMic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(PlasmaActivity.this,"Speak Up...!!!",Toast.LENGTH_SHORT).show();
//
////                if (isOpen)
////                {
////                    fabEng.startAnimation(fabClose);
////                    fabHin.startAnimation(fabClose);
////                    fabMic.startAnimation(fabRc);
////
////                    fabHin.setClickable(false);
////                    fabEng.setClickable(false);
////
////                    isOpen= false;
////
////                }
////                else
////                {
////                    fabEng.startAnimation(fabOpen);
////                    fabHin.startAnimation(fabOpen);
////                    fabMic.startAnimation(fabRa);
////
////                    fabHin.setClickable(true);
////                    fabEng.setClickable(true);
////
////                    isOpen= true;
////               }
//
//
//            }
//        });



        //On CLicks
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


        ptop.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem item) {

              if(item.getItemId()== R.id.sp) {
                  Intent intent3 = new Intent(PlasmaActivity.this,SearchActivity.class);
                  startActivity(intent3);
                  Toast.makeText(PlasmaActivity.this, "FETCHING PLASMA DONORS.....", Toast.LENGTH_SHORT).show();

              }
              return false;
          }
      });
        pbottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.phome)
                {
                    Intent intent = new Intent(PlasmaActivity.this, BloodActivity.class);
                    startActivity(intent);
                    PlasmaActivity.this.finish();
                }
                else if(menuItem.getItemId()==R.id.pmap)
                {

                    Intent intent = new Intent(PlasmaActivity.this, TrackerActivity.class);
                    startActivity(intent);

                }
                return false;
            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent= new Intent(PlasmaActivity.this,RequestPlasma.class);
                startActivity(intent);
            }
        });


        //Recycler view
        LinearLayoutManager layoutManager= new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        requestAdapter = new PRequestAdapter(requestDataModels, this);
        recyclerView.setAdapter(requestAdapter);
        populateHomepage();

    }

    private void populateHomepage()
        {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.p_get_req, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Gson gson= new Gson();
                    Type type = new TypeToken<List<PRequestDataModel>>(){}.getType();
                    List<PRequestDataModel> dataModels = gson.fromJson(response,type);

                    requestDataModels.addAll(dataModels);
                    requestAdapter.notifyDataSetChanged();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(PlasmaActivity.this, "OOps something went wrong :(", Toast.LENGTH_SHORT).show();
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

    };






