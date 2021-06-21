package com.bu.safeguard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bu.safeguard.models.modelForProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.katso.livebutton.LiveButton;

public class userProfile extends AppCompatActivity {

    EditText fullName,nickName,mail,personalPhn,energencyPhn1,emergencyPhn2  , emergencyph3 , adress;
    ImageView personalEdit , emergencyEdit  ,editInPic;
    LiveButton updateButton ;
    CircleImageView profileIMage  ;
    ProgressBar progressBar ;
    String ImageLink   , uid ;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.drawable.register);
        setContentView(R.layout.activity_profile);

        fullName = (EditText)findViewById(R.id.fullName);
        nickName = (EditText)findViewById(R.id.nikName);
        mail = (EditText)findViewById(R.id.mail);
        profileIMage = findViewById(R.id.profile_image) ;
        personalPhn = (EditText)findViewById(R.id.personalPhn);
        energencyPhn1 = (EditText)findViewById(R.id.emergencyPhn1);
        emergencyPhn2 = (EditText)findViewById(R.id.emergencyPhn2);
        progressBar  = findViewById(R.id.progrssBar );
        personalEdit = findViewById(R.id.personalInfoEdit);
        emergencyEdit = findViewById(R.id.emergenecyContactEdit);
        updateButton = findViewById(R.id.update) ;
        adress = findViewById(R.id.area) ;
        ImageView bac = findViewById(R.id.backBtn);
        emergencyph3 = findViewById(R.id.emergencyPhn3) ;
        editInPic = findViewById(R.id.ppedit);

        editInPic.setVisibility(View.GONE);
        personalEdit.setVisibility(View.GONE);
        emergencyEdit.setVisibility(View.GONE);

    //   getSupportActionBar().hide() ;


        Intent o = getIntent();
        uid = o.getStringExtra("uid");



        progressBar.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                progressBar.setVisibility(View.GONE);
            }
        } , 2000) ;

        fullName.setFocusable(false);
        nickName.setFocusable(false);
        mail.setFocusable(false);
        personalPhn.setFocusable(false);
        energencyPhn1.setFocusable(false);
        emergencyPhn2.setFocusable(false);
        adress.setFocusable(false);
        emergencyph3.setFocusable(false);

        emergencyph3.setFocusableInTouchMode(false);
        fullName.setFocusableInTouchMode(false);
        nickName.setFocusableInTouchMode(false);
        mail.setFocusableInTouchMode(false);
        personalPhn.setFocusableInTouchMode(false);
        energencyPhn1.setFocusableInTouchMode(false);
        emergencyPhn2.setFocusableInTouchMode(false);
        adress.setFocusableInTouchMode(false);


        bac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });





    }
    private  boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            return (mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting());
        } else
            return false;

    }

    private  void downloadData()
    {

        if( isConnected(userProfile.this))
        {
            try{
           //     String    uid = FirebaseAuth.getInstance().getUid() ;

                DatabaseReference mref = FirebaseDatabase.getInstance().getReference("profile").child(uid);

                mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        if(dataSnapshot.exists())
                        {
                            modelForProfile model = dataSnapshot.getValue(modelForProfile.class);

                            Picasso.get().load(model.getPpLink()).error(R.drawable.user).placeholder(R.drawable.user).into(profileIMage) ;
                            //  Picasso.get().load(model.getPpLink()).error(R.drawable.user).placeholder(R.drawable.user).into(imageViewonDrawerLayoputLayout) ;


                            ImageLink = model.getPpLink();
                            fullName.setText(model.getFullname());
                            nickName.setText(model.getNickName());
                            //  mail.setText(false);
                            personalPhn.setText(model.getPersonalPhone());
                            energencyPhn1.setText(model.getEmerph1());
                            emergencyPhn2.setText(model.getEmerph2());
                            emergencyph3.setText(model.getEmerph3());
                            adress.setText(model.getArea());
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext() , "Could not Find The Profile ." ,Toast.LENGTH_SHORT)
                                    .show();
                        }

                        //   saveTheseForOffline(model.getNickName() , model.getEmerph1()  , model.emerph2 , ImageLink) ;



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        //   getdata();
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
        else {

            //  getdata();
            Toast.makeText(getApplicationContext() , "You Are Not Connected " , Toast.LENGTH_LONG)
                    .show();



        }




    }
    @Override
    protected void onStart() {
        super.onStart();
        downloadData();



    }

}
