package com.bu.safeguard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bu.safeguard.fragment.FriendsChatFragment;
import com.bu.safeguard.fragment.GroupsFragment;
import com.bu.safeguard.fragment.NottificationFragment;
import com.bu.safeguard.models.listModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {

    ArrayList<listModel> list ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        //adding  fragment
        sectionsPagerAdapter.AddFragment(new FriendsChatFragment());
        sectionsPagerAdapter.AddFragment(new NottificationFragment());
        sectionsPagerAdapter.AddFragment(new GroupsFragment());

//        list = new ArrayList<>();
//
//
//
//        list.add(new listModel("name1" ,"name1" )) ;
//        list.add(new listModel("name2" ,"name2" )) ;
//        list.add(new listModel("name3" ,"name3" )) ;



        // lets loop out
//
//        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("test");
//        for (int counter = 0; counter < list.size(); counter++) {
//
//            HashMap map = new HashMap();
//
//           map.put("testName" , list.get(counter).getName());
//
//           if(list.get(counter).getId().equals("name2"))
//           {
//               list.remove(counter);
//           }
//
//
//            Log.d("LISTTENT"  , list.get(counter).getName()   ) ;
//
//           mref.setValue(list) ;
//
//
//
//
//        }


        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
//         int[] image = {
//                R.drawable.ic_arrow_forward_black_24dp,
//                R.drawable.ic_search,
//                R.drawable.ic_arrow_forward_black_24dp };
        tabs.setupWithViewPager(viewPager);


//
//        for (int i = 0; i < tabs.getTabCount(); i++) {
//            tabs.getTabAt(i).setIcon(image[i]);
//        }



        FloatingActionButton fab = findViewById(R.id.fab);




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  startActivity(new Intent(BaseActivity.this , NewMessageActivity.class));
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.add_friend :
                sendUserToTheAllUserAcivity() ;
                return  true  ;

            case  R.id.my_friend :
                sendUserToTheMyFriendAcivity();

                return  true ;

            default:
                return  super.onOptionsItemSelected(item);


        }



 // crash korao korao



    }

    private void sendUserToTheMyFriendAcivity() {
        Intent o = new Intent(getApplicationContext() , myFriendList.class);
        startActivity(o);
    }

    private void sendUserToTheAllUserAcivity() {
        Intent o = new Intent(getApplicationContext() , AllUserList.class);
        startActivity(o);
    }


}

