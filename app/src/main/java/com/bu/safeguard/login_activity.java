package com.bu.safeguard;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class login_activity extends AppCompatActivity {
    ImageButton google_btn;
    GoogleApiClient mGoogleApiClient;
    ProgressBar progressBar ;
    Button signInButton;
    EditText mailIn, passin;
    private boolean isGPS = true;
    TextView register  ;
    ImageButton fbButton ;
    CallbackManager callbackManager ;
    private static final String EMAIL = "email";
    private FirebaseAuth mAuth;
    private final static int RC_SIGN_IN = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.drawable.register);
        setContentView(R.layout.activity_login_activity);
        mailIn = findViewById(R.id.emailInput);
        passin = findViewById(R.id.passIn);
        signInButton = findViewById(R.id.submitBtn);
        progressBar = findViewById(R.id.progrssBar);
        google_btn = findViewById(R.id.imageView3);
        register = findViewById(R.id.textView6) ;
        fbButton = findViewById(R.id.imageButton) ;
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

      //  getSupportActionBar().hide();
        callbackManager = CallbackManager.Factory.create();
        progressBar.setVisibility(View.GONE);


        mAuth = FirebaseAuth.getInstance();



      //  printKeyHash();

        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                LoginManager.getInstance().logInWithReadPermissions(login_activity.this , Arrays.asList("email" , "public_profile"));
                LoginManager.getInstance().registerCallback( callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                // App code

                                handleFacebookAccessToken(loginResult.getAccessToken());
                            }

                            @Override
                            public void onCancel() {
                                // App code
                                Toast.makeText(getApplicationContext(), "Authentication Went Wrong \n Error :From User"  ,  Toast.LENGTH_LONG).show();

                                progressBar.setVisibility(View.GONE);

                            }

                            @Override
                            public void onError(FacebookException exception) {
                                // App code
                                Toast.makeText(getApplicationContext(), "Authentication Went Wrong \n Error :From Facebook "  ,  Toast.LENGTH_LONG).show();

                                progressBar.setVisibility(View.GONE);

                            }
                        });



            }
        }) ;

        // faceBook sign in ### GG




        // google sign in ###

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("359381789811-s51ua9492cevt99jdbhsooa6cnp2ihvh.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


                        Toast.makeText(getApplicationContext(), "Error : " + connectionResult.getErrorMessage().toString(),
                                Toast.LENGTH_LONG)
                                .show();
                        //error
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signGoogle();

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent p = new Intent(getApplicationContext(), Register.class);
                startActivity(p);

            }
        });

   /*
        phonee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), getPhoneNum.class);
               // i.putExtra("method" , "gmail");
                startActivity(i);

            }
        }) ;

    */

        signInButton.setOnClickListener(v -> {

            String mail, pass;
            mail = mailIn.getText().toString();
            pass = passin.getText().toString();

            if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(pass)) {
                Toast.makeText(getApplicationContext(), "Please Enter Something", Toast.LENGTH_SHORT)
                        .show();

            } else {
                progressBar.setVisibility(View.VISIBLE);
                proceedEmailLogin(mail, pass);
            }


        });

    }

    private void proceedEmailLogin(String mail, String pass) {

        mAuth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressBar.setVisibility(View.GONE);
                            gotToHOme();


                        } else {
                            // If sign in fails, display a message to the user.
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(login_activity.this, "Authentication failed Due To "+ task.getException().getMessage().toString() ,
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }


    public void signGoogle() {

        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {

                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthGoogle(account);
            } else {
                progressBar.setVisibility(View.GONE);
                String e = result.getStatus().toString();
                Toast.makeText(getApplicationContext(), "Authentication WENT WRONG From Gmail" + e, Toast.LENGTH_LONG).show();


            }
        }

        /// facbook  call back

        callbackManager.onActivityResult(requestCode , resultCode , data) ;


        super.onActivityResult(requestCode, resultCode, data);





    }

    private void firebaseAuthGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                           cheeckProfile();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Authentication Went Wrong \n Error : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }

                    }
                });


    }

    private void vrsomeData() {


    }

    private void gotToHOme() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);



    }
    private  void  cheeckProfile()
    {
        String  uid = FirebaseAuth.getInstance().getUid() ;

        DatabaseReference me = FirebaseDatabase.getInstance().getReference("profile").child(uid);

        me.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    progressBar.setVisibility(View.GONE);
                    gotToHOme();

                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Intent o = new Intent(getApplicationContext() , accountSetupPage.class);
                    startActivity(o);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser muer = FirebaseAuth.getInstance().getCurrentUser();
        if (muer != null) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void printKeyHash() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.metacodersbd.safeguard",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature :info.signatures){



                MessageDigest md  = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Keyhash", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


    }


    private void handleFacebookAccessToken (AccessToken token )
    {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //  Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            cheeckProfile();

                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //   Log.w(TAG, "signInWithCredential:failure", task.getException());


                            Toast.makeText(login_activity.this, "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            // updateUI(null);
                        }

                        // ...
                    }
                });
    }



}

