package com.bu.safeguard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.shashank.sony.fancytoastlib.FancyToast;

public class getPhoneNum extends AppCompatActivity {

    ImageButton nextBtn ;
    EditText phoneNumEdit ;
    String phone ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_phone_num);

        //init view
        nextBtn = findViewById(R.id.nextBtnmobileverification) ;

        phoneNumEdit = findViewById(R.id.phonenumEditText);

        // getting number from the edit text  ;

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((phoneNumEdit.getText().toString().length()) == 11 ){


                    phone ="+88"+ phoneNumEdit.getText().toString() ;

                    Intent i = new Intent(getApplicationContext() , mobileVerifiacitonActivity.class);

                    i.putExtra("PHONE" , phone);
                    i.putExtra("STATe", "nill") ;
                    startActivity(i);

                }
                else {

                    FancyToast.makeText(getPhoneNum.this,"Number Format Invalid!!",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                }

            }
        });












    }
}
