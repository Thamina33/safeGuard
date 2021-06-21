package com.bu.safeguard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bu.safeguard.models.modelForProfile;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class mapActivity extends AppCompatActivity implements OnMapReadyCallback {

    double lat , lon ;
    String name  , sosImageLink = "null", number  ;
    private DrawerLayout drawerLayout;
    NavigationView navigationView ;
    ImageView hamburger , sosImage ;
    TextView sosMeg , sosName  ;
    RelativeLayout bottomSheetLayout ;
    String uid ;
    CircleImageView profile ;
    ImageView transitionalImageView ;
    String phnumber  , sosmsgText, sosLink = "null";
    Toolbar toolbar  ;
    RelativeLayout makeAcall ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        hamburger = findViewById(R.id.hamburger);
      // sosImage = findViewById(R.id.sosImage);
        sosName = findViewById(R.id.sosName);
        profile = findViewById(R.id.profile_image) ;
        makeAcall = findViewById(R.id.HelpLayout);
        transitionalImageView =  findViewById(R.id.sosImage);

        sosMeg = findViewById(R.id.sosMsg);
        bottomSheetLayout =(RelativeLayout) findViewById(R.id.boottomshhet);




        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        navigationView = findViewById(R.id.navigation_view);

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


        makeAcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                call();

            }
        });

        transitionalImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog imageViewer = new Dialog(mapActivity.this , android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                imageViewer.requestWindowFeature(Window.FEATURE_NO_TITLE);
                imageViewer.setContentView(R.layout.image_dialouge);
                imageViewer.setCancelable(true);

                final PhotoView ImageView = imageViewer.findViewById(R.id.imageView)  ;
                final  ImageView closeBtn = imageViewer.findViewById(R.id.close) ;


                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        imageViewer.dismiss();


                    }
                });


                imageViewer.show();

                Glide.with(mapActivity.this)
                        .load(sosImageLink)
                        .placeholder(R.drawable.placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.placeholder)
                        .into(ImageView) ;

                //ImageView.setOnTouchListener(new ImageMatrixTouchHandler(mapActivity.this));







            }
        });



        hamburger.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
          /*
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);

                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                */

          finish();
            }
        });

            // getting the data from  intent

          Intent o = getIntent();
          lat = Double.valueOf(Objects.requireNonNull(o.getStringExtra("lat")))   ;
          lon = Double.valueOf(Objects.requireNonNull(o.getStringExtra("lon"))) ;
          name =o.getStringExtra("nam") ;
          uid = o.getStringExtra("uid") ;
          number = o.getStringExtra("NUM");
          sosImageLink = o.getStringExtra("sosLINK") ;
          sosmsgText = o.getStringExtra("sosMSG");
         // Toast.makeText(getApplicationContext() , sosmsgText , Toast.LENGTH_SHORT).show();

          sosName.setText(name);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext() , userProfile.class);
                i.putExtra("uid" , uid) ;
                startActivity(i);





            }
        });


        Glide.with(mapActivity.this).load(sosImageLink).into(transitionalImageView) ;



          DatabaseReference mre = FirebaseDatabase.getInstance().getReference("profile").child(uid);

          mre.addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                  modelForProfile model = dataSnapshot.getValue(modelForProfile.class) ;


                //  Picasso.get().load(model.getPpLink()).error(R.drawable.ic_person_black_24dp).into(profile)  ;
                  Glide.with(mapActivity.this).load(model.getPpLink()).error(R.drawable.ic_person_black_24dp
                  ).into(profile) ;








              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });


          if(sosImageLink.equals("null"))
          {
              if(sosmsgText.equals("null"))
              {
                  sosMeg.setVisibility(View.GONE);
                  transitionalImageView.setVisibility(View.GONE);

              }

              else {

                  sosMeg.setText(sosmsgText);
                  sosMeg.setVisibility(View.VISIBLE);
                  transitionalImageView.setVisibility(View.GONE);
                  // sosMeg.setVisibility(View.GONE);

              }


             RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) bottomSheetLayout.getLayoutParams();
             try{
                 params.height =dpToPx(100) ;
             }
             catch (Exception e )
             {
                 params.height =200 ;
             }

                params.width = LayoutParams.MATCH_PARENT ;

                bottomSheetLayout.setLayoutParams(params);



          }
          else {


              sosMeg.setText(sosmsgText);
              sosMeg.setVisibility(View.VISIBLE);


          }





        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng sydney = new LatLng(lon, lat);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title(name + " is Here Now !!!")).showInfoWindow();


        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney ));
     //   googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
   //     googleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 1300, null);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public  int dpToPx(int dp) {
        float density = getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }
    private  void call( )
    {


        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null));
        startActivity(intent);
    }



}
