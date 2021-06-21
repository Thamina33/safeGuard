package com.bu.safeguard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bu.safeguard.models.modelForProfile;
import com.bu.safeguard.models.modelForRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;
import com.thefinestartist.finestwebview.FinestWebView;

import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.samanjafari.easycountdowntimer.EasyCountDownTextview;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int SMS_PERMISSION_CODE = 5;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 10000;
    public FusedLocationProviderClient client;
    Button help, list;
    EasyCountDownTextview countDownTextView;
    String mail = "testUSer", personal_number;
    Button signOut;
    String PH1, PH2, PH3, msg, locLink, profileLink = "null", ph;
    SupportMapFragment mapFragment;
    TextView name, addresss;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");

    LinearLayout helpLayout;
    long longPressTimeout = 3000;
    Toolbar toolbar;
    String uid = "user", userImage = "null";
    ImageView cameraView, msgView,chat;
    double wayLatitude;
    double wayLongitude;
    CardView card;
    NavigationView navigationView;
    ImageView hamburger;
    CircleImageView ppimage, imageViewonDrawerLayoputLayout;
    TextView allList;
    LatLng UserLatLng = new LatLng(0, 0);
    GoogleMap MainPageMap = null;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private boolean isGPS = true;
    private boolean isStart = true;
    private long then;
    private DrawerLayout drawerLayout;

    /***
     *
     *
     *
     * @param point1 this is the 1st point
     * @param point2 his is the 2nd  point
     * @return it will return the distance between the points
     */

    public static double distanceBetween(LatLng point1, LatLng point2) {
        if (point1 == null || point2 == null) {
            return 0;
        }

        return SphericalUtil.computeDistanceBetween(point1, point2);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        help = findViewById(R.id.help);
        list = findViewById(R.id.emergencyList);
        signOut = findViewById(R.id.signOutBtn);
        helpLayout = findViewById(R.id.HelpLayout);
        card = findViewById(R.id.card);
        chat = findViewById(R.id.chat);
        countDownTextView = findViewById(R.id.easyCountDownTextview);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        hamburger = findViewById(R.id.hamburger);
        ppimage = findViewById(R.id.profile_image);
        cameraView = findViewById(R.id.camera);
        msgView = findViewById(R.id.msg);
        allList = findViewById(R.id.allList);

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,BaseActivity.class));
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        //OneSignal Initialization

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();


        navigationView = findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);
        imageViewonDrawerLayoputLayout = (CircleImageView) header.findViewById(R.id.imageONdrawerLaoout);
        name = (TextView) header.findViewById(R.id.nameOndrawerLayout);
        addresss = (TextView) header.findViewById(R.id.addressOndrawerLayout);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.menu_go_logout:

                        goToLogout();
                        return true;

                    case R.id.about_us:

                        new FinestWebView.Builder(MainActivity.this).show("http://baksoinik.org/about-us");
                        return true;
                    case R.id.member_ship:

                        new FinestWebView.Builder(MainActivity.this).show("http://baksoinik.org/membership");
                        return true;
                    case R.id.donate:

                        new FinestWebView.Builder(MainActivity.this).show("http://baksoinik.org/donate");
                        return true;
                    case R.id.Contact:

                        new FinestWebView.Builder(MainActivity.this).show("http://baksoinik.org/contact");
                        return true;

                    case R.id.my_Profile:
                        Intent io = new Intent(getApplicationContext(), Profile.class);
                        startActivity(io);
                        return true;
                    case R.id.allAlert:
                        Intent ioi = new Intent(getApplicationContext(), emergencyList.class);
                        startActivity(ioi);
                        return true;
                    case R.id.rate_us:
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=" + getPackageName())));

                        } catch (ActivityNotFoundException e) {

                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));

                        }
                        return true;

                    case R.id.share:
                        ShareCompat.IntentBuilder.from(MainActivity.this)
                                .setType("text/plain")
                                .setChooserTitle("Share App Download Link Via ")
                                .setText("This app care about my safety . "
                                        + "http://play.google.com/store/apps/details?id=" + getPackageName())
                                .startChooser();

                        return true;


                    default:
                        return true;
                }
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.vlose) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //  toolbar.setNavigationIcon(R.drawable.ic_side_nav);
        toolbar.setNavigationIcon(null);

        hamburger.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);

                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });

        client = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3 * 1000);


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        double wayLatitude = location.getLatitude();
                        double wayLongitude = location.getLongitude();

                        if (!isStart) {
                            getHelp(wayLatitude, wayLongitude);
                        } else {

                            prepareTheMap(wayLatitude, wayLongitude);

                        }


                        //     Toast.makeText(MainActivity.this, " LAt  : " + location.getLatitude() + "long "+ location.getLongitude(), Toast.LENGTH_SHORT).show();

                        //   txtLocation.setText(String.format(Locale.US, "%s -- %s", wayLatitude, wayLongitude));
                        if (client != null) {
                            client.removeLocationUpdates(locationCallback);

                            // Toast.makeText(MainActivity.this, " LAt  : "  + "long "+ location.getLongitude(), Toast.LENGTH_SHORT).show();

                        }

                    }
                }
            }
        };


        ppimage.setOnClickListener(v -> {

            Intent io = new Intent(getApplicationContext(), Profile.class);
            startActivity(io);


        });


        allList.setOnClickListener(v -> {

            Intent intent = new Intent(getApplicationContext(), emergencyList.class);
            startActivity(intent);


        });


        list.setOnClickListener(v -> {


        });

        Handler handler = new Handler();

        Runnable r = new Runnable() {
            @Override
            public void run() {

                startSendingHelp();
                Toast.makeText(getApplicationContext(), "sent help  ", Toast.LENGTH_SHORT)
                        .show();
            }
        };

        cameraView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), emergencyList.class);
                i.putExtra("link", profileLink);
                startActivity(i);


            }
        });
        msgView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isConnected(MainActivity.this)) {

                    Intent i = new Intent(getApplicationContext(), request_from_activity.class);


                    startActivity(i);

                } else {

                    Toast.makeText(getApplicationContext(), "you are not connected to internet , Please use the Help Button", Toast.LENGTH_LONG).show();


                }


            }
        });

        helpLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        card.setVisibility(View.VISIBLE);
                        countDownTextView.startTimer();

                        handler.postDelayed(r, 3000);


                        return true;
                    case MotionEvent.ACTION_UP:
                        System.out.println(" released ");
                        // realse the krake

                        countDownTextView.stopTimer();

                        card.setVisibility(View.GONE);

                        handler.removeCallbacks(r);

                        return true;
                }
                return false;
            }
        });


        help.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

            }

        });


        writeOnSharedPref();
        // vrsomeData();

    }

    private void goToLogout() {

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", true);
        editor.apply();

        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.signOut();
        Intent igfif = new Intent(getApplicationContext(), login_activity.class);
        startActivity(igfif);
        finish();
    }

    public void startSendingHelp() {
        isStart = false;

        if (!isGPS) {
            Toast.makeText(MainActivity.this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // reuqest for permission

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    AppConstants.LOCATION_REQUEST);
        } else {
            // already permission granted

            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {


                    if (location != null) {
                        double wayLatitude = location.getLatitude();
                        double wayLongitude = location.getLongitude();

                        if (!isStart) {
                            getHelp(wayLatitude, wayLongitude);
                        } else {


                            prepareTheMap(wayLatitude, wayLongitude);

                        }


                        //   Toast.makeText(MainActivity.this, " LAt  : " + location.getLatitude() + "long "+ location.getLongitude(), Toast.LENGTH_SHORT).show();

                    } else {



                        //client.requestLocationUpdates(locationRequest, locationCallback, null);

                        client.requestLocationUpdates(locationRequest,locationCallback,null);
                    }
                }
            });
        }
    }

    void check() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        ACCESS_FINE_LOCATION
                }, 1
        );
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        ACCESS_COARSE_LOCATION
                }, 1
        );


        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // reuqest for permission

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
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
                            wayLatitude = location.getLatitude();
                            wayLongitude = location.getLongitude();


                            getHelp(wayLatitude, wayLongitude);


                            //     Toast.makeText(MainActivity.this, " LAt  : " + location.getLatitude() + "long "+ location.getLongitude(), Toast.LENGTH_SHORT).show();
                        } else {

                            client.requestLocationUpdates(locationRequest, locationCallback, null);
                        }

                    });
                } else {


                    // Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                    //getTheloc();
                    prepareTheMap(wayLatitude, wayLongitude);
                    //  getTheloc() ;
                    //    recreate();
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
                } else {


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

    private void getHelp(double wayLatitude, double wayLongitude) {


        if (!isConnected(MainActivity.this)) {
            sentOfflineMsg(String.valueOf(wayLatitude), String.valueOf(wayLongitude));

        } else {

            String delegate = "hh:mm aaa";
            String Time = String.valueOf(DateFormat.format(delegate, Calendar.getInstance().getTime()));
            String DATE = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

            DATE = Time + " " + DATE;
            DatabaseReference mef = FirebaseDatabase.getInstance().getReference("reqList");
            String key = mef.push().getKey();
            //   String uid = FirebaseAuth.getInstance().getUid() ;


            // String  postid  , name  , number , uid , lon , lat , time , sosImage, sosMsg  ;
            modelForRequest model = new modelForRequest(key, mail, ph, FirebaseAuth.getInstance().getUid(), String.valueOf(wayLatitude), String.valueOf(wayLongitude), DATE, "null", "null", userImage);

            mef.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {


                    sendNotification();
                    card.setVisibility(View.GONE);
                    sendToSOSSentActivity();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    sentOfflineMsg(String.valueOf(wayLatitude), String.valueOf(wayLongitude));
                }
            });


        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
            }
        }
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
                                + "\"contents\": {\"en\": \"Alert!! " + mail + " In Danger !\"}"
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

        getTheloc();


        try {

            uid = FirebaseAuth.getInstance().getUid();

            DatabaseReference mref = FirebaseDatabase.getInstance().getReference("profile").child(uid);
            mref.keepSynced(true);
            mref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        modelForProfile model = dataSnapshot.getValue(modelForProfile.class);
                        Picasso.get().load(model.getPpLink()).error(R.drawable.user).placeholder(R.drawable.user).into(ppimage);
                        Picasso.get().load(model.getPpLink()).error(R.drawable.ic_user_circlewhite_24dp).placeholder(R.drawable.ic_user_circlewhite_24dp).into(imageViewonDrawerLayoputLayout);
                        name.setText(model.getNickName());
                        mail = model.getNickName();
                        profileLink = model.getPpLink();
                        addresss.setText(model.getArea());
                        ph = model.getPersonalPhone();

                        userImage = model.getPpLink();

                        if (isConnected(MainActivity.this)) {
                            saveTheseForOffline(model.getNickName(), model.getEmerph1(), model.getEmerph2(), userImage, model.getEmerph3(), model.getPersonalPhone());
                        } else {

                            getdata();


                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Could not Find The Profile .", Toast.LENGTH_SHORT)
                                .show();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    getdata();
                }
            });


            // Toast.makeText(getApplicationContext(), "name : " + mail  , Toast.LENGTH_SHORT)
            //    .show();
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), "Error  : ", Toast.LENGTH_SHORT)
                    .show();
        }


    }

    private void saveTheseForOffline(String nickName, String emerph1, String emerph2, String userimage, String emerph3, String personal_number) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("name", nickName);
        editor.putString("ph1", emerph1);
        editor.putString("ph2", emerph2);
        editor.putString("ph3", emerph3);
        editor.putString("pph", personal_number);
        editor.putString("uimage", userimage);


        editor.apply();
        editor.commit();


    }

    private void getTheloc() {

        check();


        // vrsomeData();
        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });

        if (!isGPS) {
            Toast.makeText(MainActivity.this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // reuqest for permission

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    AppConstants.LOCATION_REQUEST);
        } else {
            // already permission granted

            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {


                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();

                        //  getHelp(wayLatitude , wayLongitude ) ;

                        //   Toast.makeText(MainActivity.this, " LAt  : " + location.getLatitude() + "long "+ location.getLongitude(), Toast.LENGTH_SHORT).show();

                        prepareTheMap(wayLatitude, wayLongitude);
                        upadateUserCurrentLocation(wayLatitude, wayLongitude);


                    } else {

                        client.requestLocationUpdates(locationRequest, locationCallback, null);

                    }
                }
            });
        }


    }

    private void upadateUserCurrentLocation(double wayLatitude, double wayLongitude) {
        UserLatLng = new LatLng(wayLatitude, wayLongitude);

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("profile").child(uid);
        mref.keepSynced(true);
        HashMap<String, Object> map = new HashMap<>();
        map.put("currentLat", wayLatitude);
        map.put("currentLong", wayLongitude);
        mref.updateChildren(map);

        Log.d("TAG", "upadateUserCurrentLocation: " + wayLatitude);

        loadUserList() ;

    }

    private void prepareTheMap(double wayLatitude, double wayLongitude) {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(MainActivity.this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.


            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));


            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        MainPageMap = googleMap;
        LatLng sydney = new LatLng(wayLatitude, wayLongitude);
        upadateUserCurrentLocation(wayLatitude, wayLongitude);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("ME")).showInfoWindow();


        googleMap.moveCamera(CameraUpdateFactory.newLatLng(UserLatLng));
        //   googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UserLatLng, 15));
        //     googleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 1300, null);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            return (mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting());
        } else
            return false;

    }

    private void sentOfflineMsg(String lat, String lon) {
        // Now Sent Msg Via sms client but 1st load the  offline emergency number
        SharedPreferences prefs = getSharedPreferences("MyPref", MODE_PRIVATE);
        PH1 = prefs.getString("ph1", "No name defined");//"No name defined" is the default value.
        PH2 = prefs.getString("ph2", "No name defined");
        PH3 = prefs.getString("ph3", "No name defined");
        locLink = " http://maps.google.com/?q=" + lat + "," + lon;
        userImage = prefs.getString("uimage", "User");

        msg = "Help " + mail + " is in danger at " + locLink;


        // permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }

        } else {
            sentTheMsg();

        }

    }

//  debugging  phase

    private void sentTheMsg() {
        SmsManager smsManager = SmsManager.getDefault();


        if (!PH1.isEmpty()) {
            smsManager.sendTextMessage(PH1, null, msg, null, null);
        }

        Handler handlerd = new Handler();
        handlerd.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!PH2.isEmpty()) {
                    smsManager.sendTextMessage(PH2, null, msg, null, null);
                }


            }
        }, 500);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!PH3.isEmpty()) {
                    smsManager.sendTextMessage(PH3, null, msg, null, null);
                }


                sendToSOSSentActivity();
            }
        }, 1000);


    }

    private void getdata() {
        SharedPreferences prefs = getSharedPreferences("MyPref", MODE_PRIVATE);
        PH1 = prefs.getString("ph1", "No name defined");//"No name defined" is the default value.
        PH2 = prefs.getString("ph2", "No name defined");
        PH3 = prefs.getString("ph3", "No name defined");
        mail = prefs.getString("name", "User");
        ph = prefs.getString("pph", "none");
        userImage = prefs.getString("uimage", "user");

        Toast.makeText(getApplicationContext(), "Ph : " + PH1 + " PH2 : " + PH2 + "name : " + mail, Toast.LENGTH_SHORT)
                .show();


    }

    private void sendToSOSSentActivity() {

        Intent io = new Intent(getApplicationContext(), sos_sent.class);
        startActivity(io);


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing The App")
                .setMessage("Are you sure you want to close this App ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .setCancelable(false)
                .show();
    }

    public void writeOnSharedPref() {
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();

    }

    public void loadUserList() {
        List<modelForProfile> listOfProfileWhoAreArround = new ArrayList<>();


        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("profile");

        mref.keepSynced(true);
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listOfProfileWhoAreArround.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    modelForProfile user = postSnapshot.getValue(modelForProfile.class);

                    if (!user.getUid().equals(uid)) {
                        listOfProfileWhoAreArround.add(user);
                        // here you can access to name property like university.name

                        float distance = (float) distanceBetween(UserLatLng, new LatLng(user.getCurrentLat(), user.getCurrentLong()));
                        Log.d("TAG", "onDataChange:  in meter -> " + distance);
                         distance = Float.parseFloat(decimalFormat.format(distance));
                   //     if(distance >= 120 ){
                            Log.d("TAG", "onDataChange: HIgh  " + distance);

                          //     }else {

                            setPick(new LatLng(user.getCurrentLat(), user.getCurrentLong()), user.getNickName() + " " + distance + "m Away");

                     //   }


                    }
                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    public static int generate(int min,int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    private void setPick(LatLng latLng, String name) {
        try {
            float hue = generate(0 , 360) ;  //(Range: 0 to 360)
            MarkerOptions options = new MarkerOptions();
            options.title(name);
            options.position(latLng);
            options.draggable(false);
            options.icon(BitmapDescriptorFactory
                    .defaultMarker(hue));
            MainPageMap.addMarker(options);
        } catch (Exception e) {
            Log.d("TAG", "setPick: " + e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        upadateUserCurrentLocation(UserLatLng.latitude, UserLatLng.longitude);
     //   loadUserList();
    }
}

