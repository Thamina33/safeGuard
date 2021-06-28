package com.bu.safeguard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bu.safeguard.models.modelForProfile;
import com.bu.safeguard.models.modelForRequest;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.thefinestartist.finestwebview.FinestWebView;

import de.hdodenhof.circleimageview.CircleImageView;

public class emergencyList extends AppCompatActivity {
    RecyclerView recyclerView ;
    Button logOutBtn  ;
    FirebaseAuth auth ;


    GridLayoutManager gridLayoutManager ;
    ProgressBar progressBar ;
    TextView textView ;
    Toolbar toolbar  ;
    LinearLayoutManager linearLayoutManager ;
    DatabaseReference mref ;
    String uid ;
    private DrawerLayout drawerLayout;
    NavigationView navigationView ;
    ImageView hamburger  ,  errorImage,back;
    CircleImageView ppimage  , imageViewonDrawerLayoputLayout;
    TextView allList  ,addresss , name  , noData;
    FirebaseRecyclerAdapter<modelForRequest, viewholder> firebaseRecyclerAdapter ;
    FirebaseRecyclerOptions<modelForRequest> options ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_list);
        recyclerView = findViewById(R.id.listVIew) ;
        toolbar = findViewById(R.id.toolbar);
        errorImage = findViewById(R.id.image) ;

        drawerLayout = findViewById(R.id.drawer_layout);
        hamburger = findViewById(R.id.hamburger);
        back = findViewById(R.id.back);
        ppimage = findViewById(R.id.profile_image) ;
        navigationView = findViewById(R.id.navigation_view);
        noData = findViewById(R.id.noView) ;
        progressBar = findViewById(R.id.progrssBar);

        View header = navigationView.getHeaderView(0);
        imageViewonDrawerLayoputLayout = (CircleImageView) header.findViewById(R.id.imageONdrawerLaoout);
        name = (TextView) header.findViewById(R.id.nameOndrawerLayout);
        addresss = (TextView) header.findViewById(R.id.addressOndrawerLayout);
        errorImage.setVisibility(View.GONE);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.menu_go_logout:

                        goToLogout() ;
                        return true;

                    case R.id.about_us :

                        new FinestWebView.Builder(emergencyList.this).show("http://baksoinik.org/about-us");
                        return  true  ;
                    case R.id.member_ship :

                        new FinestWebView.Builder(emergencyList.this).show("http://baksoinik.org/membership");
                        return  true  ;
                    case R.id.donate :

                        new FinestWebView.Builder(emergencyList.this).show("http://baksoinik.org/donate");
                        return  true  ;
                    case R.id.Contact   :

                        new FinestWebView.Builder(emergencyList.this).show("http://baksoinik.org/contact");
                        return  true  ;

                    case  R.id.my_Profile:
                        Intent io = new Intent(getApplicationContext() , Profile.class);
                        startActivity(io);
                        return  true;
                    case  R.id.allAlert:
                       // Intent ioi = new Intent(getApplicationContext() , emergencyList.class);
                       // startActivity(ioi);
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
                        ShareCompat.IntentBuilder.from(emergencyList.this)
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        linearLayoutManager = new LinearLayoutManager(this) ;
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        loadDataFromFireBase() ;

        progressBar.setVisibility(View.VISIBLE);
        noData.setVisibility(View.GONE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {



                check();

            }
        }, 2500) ;







    }
    private  void  check() {

        if(linearLayoutManager.getItemCount()<=0)
        {
            progressBar.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
            errorImage.setVisibility(View.VISIBLE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            noData.setVisibility(View.GONE);
            errorImage.setVisibility(View.GONE);


        }

    }
    private void loadDataFromFireBase() {

        mref = FirebaseDatabase.getInstance().getReference("reqList");

        mref.keepSynced(true);

        Query query = mref.limitToLast(15);
        options = new FirebaseRecyclerOptions.Builder<modelForRequest>()

                .setQuery(query, modelForRequest.class)
                .build() ;

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<modelForRequest, viewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull viewholder viewHolderForNotes, final int i, @NonNull modelForRequest noteModel) {

                progressBar.setVisibility(View.GONE);
                noData.setVisibility(View.GONE);
                errorImage.setVisibility(View.GONE);

                viewHolderForNotes.setView(getApplicationContext() , noteModel.getName() , noteModel.getNumber() , noteModel.getUid() ,
                        noteModel.getUserImage() , noteModel.getTime());



                viewHolderForNotes.prodileImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        String  id = getItem(viewHolderForNotes.getAdapterPosition()).getUid() ;

                        Intent i = new Intent(getApplicationContext() , userProfile.class);
                        i.putExtra("uid" , id) ;
                        startActivity(i);

                    }
                });

                viewHolderForNotes.Mapbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String  lat = getItem(viewHolderForNotes.getAdapterPosition()).getLat() ;
                        String lon = getItem(viewHolderForNotes.getAdapterPosition()).getLon() ;
                        String name = getItem(viewHolderForNotes.getAdapterPosition()).getName() ;
                        String  id = getItem(viewHolderForNotes.getAdapterPosition()).getUid() ;
                        String msg = getItem(viewHolderForNotes.getAdapterPosition()).getSosMsg() ;
                        String sosLink = getItem(viewHolderForNotes.getAdapterPosition()).getSosImage() ;
                        String number = getItem(viewHolderForNotes.getAdapterPosition()).getNumber() ;

                        Intent i = new Intent(getApplicationContext() , mapActivity.class);
                        i.putExtra("lon" , lon) ;
                        i.putExtra("lat" , lat) ;
                        i.putExtra("nam" , name) ;
                        i.putExtra("uid" , id) ;
                        i.putExtra("sosMSG", msg) ;
                        i.putExtra("sosLINK" ,sosLink) ;
                        i.putExtra("NUM" ,number) ;


                        startActivity(i);



                    }
                });




            }

            @NonNull
            @Override
            public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
                final  viewholder viewHolderForNotes = new viewholder(view);




                viewHolderForNotes.setOnClickListener(new viewholder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {



                        // sending user to the edit activity





                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });




                return viewHolderForNotes;
            }
        };

        recyclerView.setLayoutManager(linearLayoutManager) ;

        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter) ;










    }

    @Override
    protected void onStart() {
        super.onStart();
        try{

            uid = FirebaseAuth.getInstance().getUid() ;



            DatabaseReference mref = FirebaseDatabase.getInstance().getReference("profile").child(uid);
            mref.keepSynced(true);
            mref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    modelForProfile model = dataSnapshot.getValue(modelForProfile.class);

                    Glide.with(emergencyList.this).load(model.getPpLink()).error(R.drawable.ic_user_circlewhite_24dp).placeholder(R.drawable.ic_user_circlewhite_24dp).into(imageViewonDrawerLayoputLayout) ;

                  //  Picasso.get().load(model.getPpLink()).error(R.drawable.user).placeholder(R.drawable.user).into(ppimage) ;
                  //  Picasso.get().load( model.getPpLink()).error(R.drawable.ic_user_circlewhite_24dp).placeholder(R.drawable.ic_user_circlewhite_24dp).into(imageViewonDrawerLayoputLayout) ;
                    name.setText(model.getNickName());
                    addresss.setText(model.getArea());


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {


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

    private void goToLogout() {

        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.signOut();
        Intent igfif  = new Intent(getApplicationContext()  , login_activity.class);
        startActivity(igfif);
        finish();
    }
}
