package com.e.blood_bank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
/*
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.bumptech.glide.Glide;
*/
import com.e.blood_bank.Utils.Endpoints;
import com.e.blood_bank.Utils.VolleySingleton;

//import com.e.blood_bank.Utils.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class Request_Blood extends AppCompatActivity {

    // variables

    private Button post;
    private EditText reqmessage;

    // private CircleImageView profile;
    // private TextView upload;

    //Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request__blood);

        // Android networking code
        // AndroidNetworking.initialize(getApplicationContext());

        //Initialize
        post = findViewById(R.id.PostReq);
        reqmessage = findViewById(R.id.messageReq);

        //profile= findViewById(R.id.profile);
        //upload = findViewById(R.id.upload);

        //On click listeners

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValid()) {
                    //Code
                    // uploadRequest(reqmessage.getText().toString);
                    String rmessage, number;

                    number = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("number", "12345");
                    rmessage = reqmessage.getText().toString();

                    reqpost(number, rmessage);

                    Intent intent = new Intent(Request_Blood.this, BloodActivity.class);
                    startActivity(intent);
                    Request_Blood.this.finish();

                }

            }
        });
    }

    //Using volley below

    private void reqpost(final String number, final String rmessage) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.upload_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(Request_Blood.this, "Success", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Request_Blood.this, "OOPS :( Something went WRONG", Toast.LENGTH_SHORT).show();
                // error message print
                Log.d("VOLLEY", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("message", rmessage);
                params.put("number", number); // These key k are the vales from php file


                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }


    private boolean isValid() {
        if (reqmessage.getText().toString().isEmpty()) {
            showMessage("Message field can not be blank");
            return false;
        }
        /*
        else if (imageUri==null)
        {
            showMessage("Pick Image");
            return false;

        }
        */
        return true;

    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }





        /*
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            // For choosing image from files
            public void onClick(View view) {
              permission();
            }
        });

    */
    //Functions

    /*
    private void pickImage()
    {
        Intent intent= new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,101);

    }

    private void permission() {
        if (PermissionChecker.checkSelfPermission(getApplicationContext(),READ_EXTERNAL_STORAGE)
             !=PermissionChecker.PERMISSION_GRANTED)
        {
            // Asking for permission
            requestPermissions(new String[]{READ_EXTERNAL_STORAGE},401);

        }
        else
        {
            //If permission is there
            pickImage();

        }
    }

       @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==401)
        {
            if(grantResults[0]==PermissionChecker.PERMISSION_GRANTED)
            {
                //Persmission was granted
                pickImage();

            }
            else
                showMessage("Permssion Denied");
        }
    }

  */

/*
//Using android networking

    private void uploadRequest(String message)
    {
        // Later to upload message
        // Can use volley too but its complicated
        //We used Fast Android Networking

        //Path
        String path= "";
        try{

            path =getPath(imageUri);

        } catch (URISyntaxException e) {
            showMessage("Wrong url");
        }
        //importing number
        String number = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("number","12345");
        //Copied android networking to upload to server
        AndroidNetworking.upload(Endpoints.uploadreq)
                .addMultipartFile("file",new File(path))
                .addQueryParameter("message" ,message)
                .addQueryParameter("number",number)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                        long progress =((bytesUploaded* 100)/totalBytes);//%age
                        upload.setText(String.valueOf(progress+"%"));
                        upload.setOnClickListener(null);

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                           if( response.getBoolean("success"))
                           {
                               showMessage("Succesfull");
                               Request_Blood.this.finish();
                           }
                           else {
                               showMessage(response.getString("message"));
                           }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==101 && resultCode==RESULT_OK)
        {
            if(data!=null)
            {
                imageUri=data.getData();
                Glide.with(getApplicationContext()).load(imageUri).into(profile);
            }
        }
    }

// Path for image

@SuppressLint("NewApi")
private String getPath(Uri uri) throws URISyntaxException {
    final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
    String selection = null;
    String[] selectionArgs = null;
    // Uri is different in versions after KITKAT (Android 4.4), we need to
    // deal with different Uris.
    if (needToCheckUri && DocumentsContract.isDocumentUri(getApplicationContext(), uri)) {
        if (isExternalStorageDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            return Environment.getExternalStorageDirectory() + "/" + split[1];
        } else if (isDownloadsDocument(uri)) {
            final String id = DocumentsContract.getDocumentId(uri);
            uri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
        } else if (isMediaDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];
            if ("image".equals(type)) {
                uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }
            selection = "_id=?";
            selectionArgs = new String[]{
                    split[1]
            };
        }
    }
    if ("content".equalsIgnoreCase(uri.getScheme())) {
        String[] projection = {
                MediaStore.Images.Media.DATA
        };
        Cursor cursor = null;
        try {
            cursor = getContentResolver()
                    .query(uri, projection, selection, selectionArgs, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.moveToFirst()) {
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
        }
    } else if ("file".equalsIgnoreCase(uri.getScheme())) {
        return uri.getPath();
    }
    return null;
}


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    }

    */

}