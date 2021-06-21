package com.bu.safeguard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bu.safeguard.models.modelForProfile;
import com.bu.safeguard.models.modelForRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.util.UUID;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.katso.livebutton.LiveButton;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class request_from_activity extends AppCompatActivity {
    ImageView sosimagechooser , addicon ;
    LiveButton askForLiveBtn ;
    StorageReference mStorageReference ;
    ProgressDialog mprogressDialog ;
    private Bitmap compressedImageFile;
    Uri mFilePathUri ;
    DatabaseReference mref ;
   String PH1  , PH2  , PH3 , locLink , msg , personalPh , userImage = "null";
    String uid , mail    , sosmsg;
    String name = null ;


    private static final int SMS_PERMISSION_CODE = 5;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =10000 ;
    public FusedLocationProviderClient client;
    Button help , list ;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    String  imageLink = "null" ;
    EditText sosmsgInput ;
    LiveButton sendHelp ;
    ProgressBar pbar ;

    private boolean isGPS = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_from_activity);
        askForLiveBtn = findViewById(R.id.askForHelpBtn) ;
        sosimagechooser = findViewById(R.id.sosImageChooser);
        addicon = findViewById(R.id.sosImageaddIcon);
        sosmsgInput = findViewById(R.id.sosnote) ;
        pbar = findViewById(R.id.progress_bar);
        pbar.setVisibility(View.GONE);
        ImageView back = findViewById(R.id.back);




       // getSupportActionBar().hide();

        //pregress dialog
        mprogressDialog = new ProgressDialog(request_from_activity.this  );
        mStorageReference = FirebaseStorage.getInstance().getReference("emergencyPic");
        mref = FirebaseDatabase.getInstance().getReference("reqList");
        check();




        // vrsomeData();
        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        client = LocationServices.getFusedLocationProviderClient(request_from_activity.this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(20 * 1000);




        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        double   wayLatitude = location.getLatitude();
                        double    wayLongitude = location.getLongitude();

                        getHelp(wayLatitude , wayLongitude ) ;

                        //     Toast.makeText(MainActivity.this, " LAt  : " + location.getLatitude() + "long "+ location.getLongitude(), Toast.LENGTH_SHORT).show();

                        //   txtLocation.setText(String.format(Locale.US, "%s -- %s", wayLatitude, wayLongitude));
                        if (client != null) {
                            client.removeLocationUpdates(locationCallback);
                        }

                    }
                }
            }
        };



        sosimagechooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(request_from_activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(request_from_activity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                        BringImagePicker();



                    } else {

                        BringImagePicker();

                    }

                } else {

                    BringImagePicker();

                }


            }
        });


      askForLiveBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {


            if(mFilePathUri == null)
            {

             imageLink = "null" ;

            }

            getThecornnidates(imageLink);



          }
      });
    }



    private void getHelp(double wayLatitude, double wayLongitude) {
     sosmsg=sosmsgInput.getText().toString() ;
     if(TextUtils.isEmpty(sosmsg))
     {
         sosmsg = "null" ;
     }
        String delegate = "hh:mm aaa";
        String  Time = String.valueOf(DateFormat.format(delegate, Calendar.getInstance().getTime()));
     String   DATE = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        DATE = Time + " "+ DATE;


        pbar.setVisibility(View.VISIBLE);
        DatabaseReference mef = FirebaseDatabase.getInstance().getReference("reqList");
        String key = mef.push().getKey();
           String uid = FirebaseAuth.getInstance().getUid() ;

        // String  postid  , name  , number , uid , lon , lat , time , sosImage, sosMsg  ;
        modelForRequest model =  new modelForRequest(key , mail, personalPh ,  uid ,String.valueOf(wayLatitude)  ,String.valueOf(wayLongitude)  , DATE  , imageLink , sosmsg, userImage) ;

        mef.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                pbar.setVisibility(View.GONE);
               sendNotification();
               sendToSOSSentActivity();



            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pbar.setVisibility(View.GONE);
                getdata();
                sentOfflineMsg(String.valueOf(wayLatitude) ,String.valueOf(wayLongitude) );
                //this is failed
            }
        }) ;





    }

    private void BringImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.RECTANGLE) //shaping the image
                .start(request_from_activity.this);
    }
    @Override
    protected void onActivityResult(/*int requestCode, int resultCode, @Nullable Intent data*/
            int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mFilePathUri = result.getUri();

                    addicon.setVisibility(View.GONE);

                    sosimagechooser.setImageURI(mFilePathUri);




                //sending data once  user select the image
              // uploadPicToServer(mFilePathUri) ;
                uploadPicToCusServer(mFilePathUri);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Toast.makeText(this, error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }


    }


    private void uploadPicToServer() {

        if(mFilePathUri != null)
        {
            final String randomName = UUID.randomUUID().toString();

            // PHOTO UPLOAD
            File newImageFile = new File(mFilePathUri.getPath());

//            try {
//
//                compressedImageFile = new Compressor(request_from_activity.this)
//                        .setMaxHeight(900)
//                        .setMaxWidth(900)
//                        .setQuality(50)
//                        .compressToBitmap(newImageFile);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] imageData = baos.toByteArray();
            UploadTask filePath = mStorageReference.child(randomName+uid + ".jpg").putBytes(imageData);

            filePath.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    Uri downloaduri = uriTask.getResult();

                      imageLink = downloaduri.toString() ;




                     mprogressDialog.hide();

                   // mref.child(ts).setValue(downloaduri.toString());


                  //  sentToPrevActivity();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mprogressDialog.hide();
                    Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_LONG).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                    mprogressDialog.setTitle("Uploading.......");
                    mprogressDialog.show();


                }
            });


        }


    }

    private void getThecornnidates(String imageLink) {


        if (!isGPS) {
            Toast.makeText(this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // reuqest for permission

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    AppConstants.LOCATION_REQUEST);
        } else {
            // already permission granted

            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {



                    if (location != null) {
                        double    wayLatitude = location.getLatitude();
                        double   wayLongitude = location.getLongitude();

                      getHelp(wayLatitude , wayLongitude ) ;


                        //   Toast.makeText(MainActivity.this, " LAt  : " + location.getLatitude() + "long "+ location.getLongitude(), Toast.LENGTH_SHORT).show();

                    }
                    else {

                        client.requestLocationUpdates(locationRequest, locationCallback, null);
                    }
                }
            })  ;
        }



    }
    void check(){
        ActivityCompat.requestPermissions(this , new String[]{
                        ACCESS_FINE_LOCATION
                } ,1
        );
        ActivityCompat.requestPermissions(this , new String[]{
                        ACCESS_COARSE_LOCATION
                } ,1
        );



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // reuqest for permission

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    AppConstants.LOCATION_REQUEST);
        }




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    client.getLastLocation().addOnSuccessListener(this, location -> {
                        if (location != null) {
                            double    wayLatitude = location.getLatitude();
                            double   wayLongitude = location.getLongitude();


                            getHelp(wayLatitude , wayLongitude ) ;


                            //     Toast.makeText(MainActivity.this, " LAt  : " + location.getLatitude() + "long "+ location.getLongitude(), Toast.LENGTH_SHORT).show();
                        }
                        else {

                            client.requestLocationUpdates(locationRequest, locationCallback, null);
                        }

                    });
                } else {
                    Toast.makeText(request_from_activity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;



            }
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // msg sent here ;
                    sentTheMsg();
                    // smsManager.sendTextMessage(phoneNo, null, message, null, null);

                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                }
                else {


                    return;
                }
            }
            case SMS_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // SMS related task you need to do.

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }


    }

    private  void sentOfflineMsg(String lat ,String lon) {
        // Now Sent Msg Via sms client but 1st load the  offline emergency number
        SharedPreferences prefs = getSharedPreferences("MyPref", MODE_PRIVATE);
        PH1 = prefs.getString("ph1", "No name defined");//"No name defined" is the default value.
        PH2 = prefs.getString("ph2", "No name defined");
        PH3 = prefs.getString("ph3", "No name defined");
        locLink = " http://maps.google.com/?q=" + lat + ","+ lon  ;
        userImage = prefs.getString("uimage" , "User") ;
        mail = prefs.getString("name" , "User") ;
        msg = "Help "+ mail +" is in danger at "+  locLink ;




        // permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {



            }

            else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }

        }
        else {
            sentTheMsg();

        }

    }

    private void sentTheMsg() {
        SmsManager smsManager = SmsManager.getDefault();
        if (!PH1.isEmpty())
        {

            smsManager.sendTextMessage(PH1, null, msg, null, null);

        }
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!PH2.isEmpty())
                {    smsManager.sendTextMessage(PH2, null, msg, null, null);

                }

            }
        } , 500) ;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!PH3.isEmpty())
                {
                    smsManager.sendTextMessage(PH3, null, msg, null, null);

                }

                sendToSOSSentActivity();
            }
        } , 1000) ;

    }
    private void sendNotification() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    //    String send_email;

                    //This is a Simple Logic to Send Notification different Device Programmatically....
                    //   send_email = "admin@gmail.com";


                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic MTFiMjQ4ODUtMmY1NC00MmY5LWE5ZDItMDY0NTExM2UzMTE2");
                        con.setRequestMethod("POST");


                        String strJsonBody = "{"
                                + "\"app_id\": \"33ec6730-750a-4579-bc4d-b8e0483a1b4c\","
                                + "\"included_segments\": [\"Subscribed Users\"],"
                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \"Alert!! "+ mail  +" In Danger !\"}"
                                + "}";


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();




            try{
                uid = FirebaseAuth.getInstance().getUid() ;



                DatabaseReference mref = FirebaseDatabase.getInstance().getReference("profile").child(uid);

                mref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        modelForProfile model = dataSnapshot.getValue(modelForProfile.class);

                       // Picasso.get().load(model.getPpLink()).error(R.drawable.user).into(ppimage) ;
                     //   Picasso.get().load(model.getPpLink()).error(R.drawable.user).into(imageViewonDrawerLayoputLayout) ;
                        name= model.getNickName();
                        mail = model.getNickName();
                        personalPh = model.getPersonalPhone() ;
                        userImage = model.getPpLink() ;





                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        getdata();
                    }
                });


                // Toast.makeText(getApplicationContext(), "name : " + mail  , Toast.LENGTH_SHORT)
                //    .show();
            }
            catch (NullPointerException e )
            {
                Toast.makeText(getApplicationContext(), "Error  : "  , Toast.LENGTH_SHORT)
                        .show();
            }


    }
    private  void getdata(){
        SharedPreferences prefs = getSharedPreferences("MyPref", MODE_PRIVATE);
        PH1 = prefs.getString("ph1", "No name defined");//"No name defined" is the default value.
        PH2 = prefs.getString("ph2", "No name defined");
        PH3 = prefs.getString("ph3", "No name defined");
        mail = prefs.getString("name" , "User") ;
        personalPh = prefs.getString("pph", "none") ;
        userImage = prefs.getString("uimage" , "User") ;

        Toast.makeText(getApplicationContext() , "Ph : " + PH1 + " PH2 : " + PH2  + "name : "+ mail, Toast.LENGTH_SHORT)
                .show();



    }
    private  void sendToSOSSentActivity()
    {

        Intent io = new Intent(getApplicationContext() , sos_sent.class);
        startActivity(io);
        finish();


    }

    private void uploadPicToCusServer(Uri mFilePathUri) {

        mprogressDialog.show();
        mprogressDialog.setMessage("Uploading New Image");

        // call for network

        File file = new File(mFilePathUri.getPath());

        File compressed ;

        try {
            compressed = new Compressor(this)
                    .setMaxHeight(700)
                    .setMaxWidth(700)
                    .setQuality(55)
                    .compressToFile(file);
        }
        catch (Exception e )
        {
            compressed =  file  ;
        }
        //creating request body for file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), compressed);
        //  RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), desc);
        //The gson builder
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        //creating retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(constants.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //creating our api
        api api = retrofit.create(api.class);

        Call<UploadResult> call = api.uploadImage(requestFile) ;

        call.enqueue(new Callback<UploadResult>() {
            @Override
            public void onResponse(Call<UploadResult> call, Response<UploadResult> response) {


                if(response.code() == 200 && response.body() != null)
                {
                    UploadResult result =  response.body() ;


                    imageLink = constants.DWLDURL + result.getMsg().toString() ;

                    mprogressDialog.dismiss();
                }
                else
                {
                    mprogressDialog.dismiss();

                    Toast.makeText(getApplicationContext() , "SomeTHing Went Wrong. Please  Try Again !" , Toast.LENGTH_LONG)
                            .show();
                }

            }

            @Override
            public void onFailure(Call<UploadResult> call, Throwable t) {
                Log.d("RRR" , t.getMessage().toUpperCase().toString()) ;

                mprogressDialog.dismiss();

                Toast.makeText(getApplicationContext() , "SomeTHing Went Wrong Please  Try Again" , Toast.LENGTH_LONG)
                        .show();
            }
        }) ;







    }
}
