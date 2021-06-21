package com.bu.safeguard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OtpTextView;
import ir.samanjafari.easycountdowntimer.CountDownInterface;
import ir.samanjafari.easycountdowntimer.EasyCountDownTextview;

public class mobileVerifiacitonActivity extends AppCompatActivity {
    EasyCountDownTextview countDownTextView ;
    TextView resendText ;
    String phone ;
    ImageButton backButton ;



    private String verificationid;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    FirebaseAuth.AuthStateListener mAuthListener ;
    private OtpTextView editText;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks ;
    ImageButton signInBtn ;
    FirebaseUser mUser ;
    String State ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verifiaciton);

        getSupportActionBar().hide();
        //receiving  phone number from  the previous activity
        Intent o = getIntent();
        phone = o.getStringExtra("PHONE") ;
        State = o.getStringExtra("STATe");

        sendVerificationCode(phone);
        //init view
        countDownTextView = findViewById(R.id.easyCountDownTextview) ;
        resendText = findViewById(R.id.resendTv) ;
        backButton = findViewById(R.id.backbtn);
        signInBtn = findViewById(R.id.signin_btn) ;
        editText = findViewById(R.id.otp_view) ;
        progressBar = findViewById(R.id.progrssBar);


        mAuth = FirebaseAuth.getInstance();
        //setting my views
        countDownTextView.setVisibility(View.VISIBLE);
        resendText.setVisibility(View.GONE);
        countDownTextView.setOnTick(new CountDownInterface() {
            @Override
            public void onTick(long time) {

            }

            @Override
            public void onFinish() {

                countDownTextView.setVisibility(View.GONE);
                resendText.setVisibility(View.VISIBLE);


            }
        });


        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String code = editText.getOTP();

                if ((code.isEmpty() || code.length() < 6)){

                    //  editText.setError("Enter code...");
                    Toast.makeText(getApplicationContext() , "PLease Enter The 6 Digit Code Properly" , Toast.LENGTH_SHORT)
                            .show();
                }
                // progressBar.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);

            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();


            }
        });

        resendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendText.setVisibility(View.GONE);
                countDownTextView.setVisibility(View.VISIBLE);
                sendVerificationCode(phone);
                countDownTextView.startTimer();
            }
        });


    }


    private void verifyCode(String code){
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, code);
            signInWithCredential(credential);
        }
        catch (Exception e){
            progressBar.setVisibility(View.INVISIBLE);
            Log.i("Error : ", " " + e.getMessage());
            Toast toast = Toast.makeText(this, "Error   "+  e.getMessage(), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }
    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            if(State.contains("LOGIN")){
                                Intent intent = new Intent(mobileVerifiacitonActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            }
                            else {

                                Intent intent = new Intent(mobileVerifiacitonActivity.this, MainActivity.class);
                                intent.putExtra("PHONE" , phone);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }




                        } else {

                            Toast.makeText(mobileVerifiacitonActivity.this,"Error: "+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    private void sendVerificationCode(String number){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationid = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){


                verifyCode(code);
            }

            else {

                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(mobileVerifiacitonActivity.this,"Error: wrong Code  ", Toast.LENGTH_LONG).show();

            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(mobileVerifiacitonActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();

        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){

            Intent intent = new Intent(mobileVerifiacitonActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

    }
}
