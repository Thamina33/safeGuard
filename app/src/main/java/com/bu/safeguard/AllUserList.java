package com.bu.safeguard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bu.safeguard.models.FriendRequestModel;
import com.bu.safeguard.models.modelForProfile;
import com.bu.safeguard.viewHolders.viewholderForAllUser;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class AllUserList extends AppCompatActivity {

    RecyclerView recyclerView ;
    LinearLayoutManager llm ;
    DatabaseReference userRef ;
    FirebaseRecyclerAdapter<modelForProfile, viewholderForAllUser> firebaseRecyclerAdapter ;
    FirebaseRecyclerOptions< modelForProfile> options ;
    // manifest  update korco ki
//    android.content.ActivityNotFoundException: Unable to find explicit activity
//    class {com.bu.safeguard/com.bu.safeguard.AllUserList}; have you declared this activity in your AndroidManifest.xml?
    //see

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user_list);

        userRef = FirebaseDatabase.getInstance().getReference("profile"); //seriously toma

        userRef.keepSynced(true); // cahce enabled

        // views

        recyclerView = findViewById(R.id.list) ;
        llm = new LinearLayoutManager(AllUserList.this);
        recyclerView.setLayoutManager(llm) ;



        loadData() ;
    }

    private void loadData() {


      //  Query query = userRef.orderByChild("TimeStamp");

        options = new FirebaseRecyclerOptions.Builder< modelForProfile>()
                .setQuery( userRef ,  modelForProfile.class)
                .build() ;


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter< modelForProfile, viewholderForAllUser>(options) {
            @Override
            protected void onBindViewHolder(@NonNull viewholderForAllUser viewholderForAllUser, int i, @NonNull final  modelForProfile userModel) {

                viewholderForAllUser.setDetails( getApplicationContext() , userModel.getNickName() , userModel.getPpLink());

                viewholderForAllUser.setOnClickListener(new viewholderForAllUser.ClickListener() {
                    @Override
                    public void onItemClick(View view, final  int position) {


                        String targetuid = getItem(position).getUid() ;

                     //   Toast.makeText(getApplicationContext()  , "" +   , Toast.LENGTH_LONG).show();
                        sendFriendReq(targetuid) ;



                    }
                });


            }

            @NonNull
            @Override
            public viewholderForAllUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_for_user_list, parent, false);

                 viewholderForAllUser viewholder = new viewholderForAllUser(view) ;




                return viewholder;
            }


        } ;

        recyclerView.setLayoutManager(llm) ;
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter) ;

    }

    private void sendFriendReq(final String targetuid) {

        // TODO uid  manual
        final String Own_uid = FirebaseAuth.getInstance().getUid() ;

        DatabaseReference  reqRef = FirebaseDatabase.getInstance().getReference("profile")
                .child(targetuid)
                .child("reqRepo");

   final String PostKey = reqRef.push().getKey() ;

        //   String  requsetedFriendUsername, requsetedFriendUid, timestamp,  requsetedFriendProfileLink, postId;
        FriendRequestModel model = new FriendRequestModel( "****" ,Own_uid
        ,  System.currentTimeMillis() /1000 + "" ,
                "https://picsum.photos/200/300" , PostKey  ) ;



        reqRef.child(PostKey).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

//                Toast.makeText(getApplicationContext() ,
//                        "Req Sent !!!" , Toast.LENGTH_SHORT).show();
                DatabaseReference  OwnRef = FirebaseDatabase.getInstance().getReference("profile")
                        .child(Own_uid)
                        .child("sentReqRepo");

                HashMap <String, String> map = new HashMap<String, String>();

                map.put("sendReqUID" ,targetuid ) ;
                map.put("status"  , "Pending") ;

                OwnRef.child(PostKey).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(getApplicationContext() , "Req Sent !!!" , Toast.LENGTH_SHORT).show();
                    }
                }) ;




            }
        }) ;








    }
}
