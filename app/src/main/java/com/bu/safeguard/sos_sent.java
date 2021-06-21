package com.bu.safeguard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bu.safeguard.models.modelForProfile;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class sos_sent extends AppCompatActivity {
    RelativeLayout  call99 , call10 , ambulance  ;
    String number = "999" ;


    private DrawerLayout drawerLayout;
    NavigationView navigationView ;
    ImageView hamburger ;
    Toolbar toolbar ;
    CircleImageView ppimage  , imageViewonDrawerLayoputLayout;
    TextView name , addresss ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos_sent);



        call10 = findViewById(R.id.call102Layout);
        call99 = findViewById(R.id.call999Layout);
        ppimage = findViewById(R.id.profile_image);
        ambulance = findViewById(R.id.callambulanceLayout);

        drawerLayout = findViewById(R.id.drawer_layout);
        hamburger = findViewById(R.id.hamburger);


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

                        goToLogout() ;
                        return true;

                    case  R.id.my_Profile:
                        Intent io = new Intent(getApplicationContext() , Profile.class);
                        startActivity(io);
                        return  true;
                    case  R.id.allAlert:
                        Intent ioi = new Intent(getApplicationContext() , emergencyList.class);
                        startActivity(ioi);
                        return  true;
                    case R.id.rate_us :
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=" + getPackageName())));

                        } catch (ActivityNotFoundException e) {

                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(  "https://play.google.com/store/apps/details?id=" + getPackageName())));

                        }
                        return  true;

                    case R.id.share:
                        ShareCompat.IntentBuilder.from(sos_sent.this)
                                .setType("text/plain")
                                .setChooserTitle("Share App Download Link Via ")
                                .setText("This app care about my safety . "
                                        +"http://play.google.com/store/apps/details?id=" + getPackageName())
                                .startChooser();

                        return  true;


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





        call99.setOnClickListener(v -> {

            number = "999" ;
            call();
        });

        call10.setOnClickListener(v -> {

            number = "+88102" ;
            call();

        });

        ambulance.setOnClickListener(v -> {

            number = "999" ;
        call() ;

        });
    }
     private  void call( )
     {
         Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null));

         startActivity(intent);

     }


    private void goToLogout() {

        try {
            FirebaseAuth mauth = FirebaseAuth.getInstance();
            mauth.signOut();
            Intent igfif  = new Intent(getApplicationContext()  , login_activity.class);
            startActivity(igfif);
            finish();
        }
        catch (Exception e )
        {
            Toast.makeText(getApplicationContext()  , "Error "+ e.getMessage() , Toast.LENGTH_LONG)
                    .show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

            try{
                     String    uid = FirebaseAuth.getInstance().getUid() ;



                DatabaseReference mref = FirebaseDatabase.getInstance().getReference("profile").child(uid);
                mref.keepSynced(true);
                mref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        modelForProfile model = dataSnapshot.getValue(modelForProfile.class);

                        Picasso.get().load(model.getPpLink()).error(R.drawable.user).placeholder(R.drawable.user).into(ppimage) ;
                        Picasso.get().load(model.getPpLink()).error(R.drawable.ic_user_circlewhite_24dp).placeholder(R.drawable.ic_user_circlewhite_24dp).into(imageViewonDrawerLayoputLayout) ;
                        name.setText(model.getNickName());


                        addresss.setText(model.getArea());







                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                       // getdata();
                        Toast.makeText(getApplicationContext(), "Your Are Offline " , Toast.LENGTH_SHORT).show();
                    }
                });


                // Toast.makeText(getApplicationContext(), "name : " + mail  , Toast.LENGTH_SHORT)
                //    .show();
            }
            catch (NullPointerException e )
            {
                Toast.makeText(getApplicationContext(), "Error  : You are Offline"  , Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }

