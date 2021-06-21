package com.bu.safeguard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    FirebaseAuth mAuth ;
    ProgressBar pbar ;

    EditText mail,pass,confirmPass;
    Button nextBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.drawable.register);
        setContentView(R.layout.activity_register);

        mail = findViewById(R.id.mail);
        pass = findViewById(R.id.pass);
        confirmPass= findViewById(R.id.confirmPass);
        nextBtn= findViewById(R.id.nextBtn);
        mAuth = FirebaseAuth.getInstance() ;
        pbar = findViewById(R.id.progrssBar);

        getSupportActionBar().hide();
        pbar.setVisibility(View.GONE);
        
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                String maiL  = mail.getText().toString();
                String passs = pass.getText().toString() ; 
                String conpas = confirmPass.getText().toString() ; 
                
                if(TextUtils.isEmpty(maiL) || TextUtils.isEmpty(passs) ||TextUtils.isEmpty(conpas)){


                    Toast.makeText(getApplicationContext(), "Error, Please Fill The Data Properly" , Toast.LENGTH_SHORT)
                            .show();
                    
                    
                }
                else {
                    

                    if (passs.equals(conpas))
                    {
                        pbar.setVisibility(View.VISIBLE);
                        registerUser(maiL, passs) ;

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Error, Password is not Same" , Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                
                
                
                
            }
        });


    }

    private void registerUser(String maiL, String passs) {


        mAuth.createUserWithEmailAndPassword(maiL, passs)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            pbar.setVisibility(View.GONE);
                            Intent o = new Intent(getApplicationContext() , accountSetupPage.class);
                            startActivity(o);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            pbar.setVisibility(View.GONE);
                            Toast.makeText(Register.this, "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }
}
