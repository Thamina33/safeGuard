package com.bu.safeguard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bu.safeguard.models.modelForProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class accountSetupPage extends AppCompatActivity {

    EditText namein, nickNamein, phonein, emerPh1in, emerph2in, areain, emrgenyph3;
    String name, nickName, phone, emerph1, emerph2, emerph3, area, userImagE;

    Button submitBtn;

    FirebaseAuth mauth;
    CircleImageView proPicChooser;
    ProgressDialog mprogressDialog;
    String uid;
    StorageReference mStorageReference;

    private Bitmap compressedImageFile;
    Uri mFilePathUri;
    DatabaseReference mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.drawable.register);
        setContentView(R.layout.activity_account_set_up);
        // init views ;

        namein = findViewById(R.id.fullName);
        nickNamein = findViewById(R.id.nickName);
        proPicChooser = findViewById(R.id.profile_image);
        phonein = findViewById(R.id.pContact);
        emerPh1in = findViewById(R.id.eContact1);
        emerph2in = findViewById(R.id.eContact2);
        areain = findViewById(R.id.adress);
        submitBtn = findViewById(R.id.submitBtn);
        emrgenyph3 = findViewById(R.id.eContact3);

        //    getSupportActionBar().hide();
        // tick the camera


       mauth = FirebaseAuth.getInstance();
       uid = mauth.getUid();

        //progress dialog
        mprogressDialog = new ProgressDialog(accountSetupPage.this);
        mprogressDialog.setCancelable(false);

       mStorageReference = FirebaseStorage.getInstance().getReference("profilePics").child(uid);
       mref = FirebaseDatabase.getInstance().getReference("profile").child(uid);


       ChekcUserHasID() ;


        proPicChooser.setOnClickListener(v -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (ContextCompat.checkSelfPermission(accountSetupPage.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(accountSetupPage.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    BringImagePicker();


                } else {

                    BringImagePicker();

                }

            } else {

                BringImagePicker();

            }


        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //   EditText namein , nickNamein , phonein , emerPh1in , emerph2in , areain  ;
                //  String name , nickName , phone , emerph1 , emerph2  , area ;
                name = namein.getText().toString();
                nickName = nickNamein.getText().toString();
                phone = phonein.getText().toString();
                emerph1 = emerPh1in.getText().toString();
                emerph2 = emerph2in.getText().toString();
                emerph3 = emrgenyph3.getText().toString();

                area = areain.getText().toString();

                if(mFilePathUri != null)
                {
                    Log.d("DATA" , mFilePathUri + "" ) ;



                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(nickName) || TextUtils.isEmpty(phone)
                            || TextUtils.isEmpty(emerph1) || TextUtils.isEmpty(area) || TextUtils.isEmpty(emerph2)) {
                        Toast.makeText(accountSetupPage.this, " Please Fill the Data Properly."
                                , Toast.LENGTH_SHORT).show();

                    }
                    else {

                        // upload image to the server
                        uploadPP(mFilePathUri) ;

                        //   uploadDataToTheServer();


                    }
                }
                else
                {
                    Toast.makeText(accountSetupPage.this, " Please Select Your  Profile Image Properly.",
                            Toast.LENGTH_SHORT).show();

                }





            }
        });


    }

    private void ChekcUserHasID() {

        ProgressDialog dialog = new ProgressDialog(accountSetupPage.this);
        dialog.setMessage("Checking For Account");
        dialog.show();

        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    modelForProfile model = dataSnapshot.getValue(modelForProfile.class) ;

                    // save it on the off line
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("name", model.getNickName());
                    editor.putString("ph1", model.getEmerph1());
                    editor.putString("ph2", model.getEmerph2());
                    editor.putString("ph3", model.getEmerph3());
                    editor.putString("pph", model.getPersonalPhone());
                    editor.putString("uimage", model.getPpLink());


                    editor.apply();
                    editor.commit();
                    mprogressDialog.hide();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();

                }
                else
                {
                    dialog.dismiss() ;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void uploadPP(Uri  fileUri ){

        mprogressDialog.setMessage("Uploading Data....");
        mprogressDialog.show();
   //     File file = new File(getRealPathFromURI(fileUri));

        File file = new File(mFilePathUri.getPath());

        File compressed ;

        try {
            compressed = new Compressor(this)
                    .setMaxHeight(600)
                    .setMaxWidth(600)
                    .setQuality(50)
                    .compressToFile(file);
        }
        catch (Exception e )
        {
            compressed =  file  ;
        }

        //creating request body for file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), compressed);
      //  RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), desc);
        //The gson builder
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        //creating retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(constants.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //creating our api
        api api = retrofit.create(api.class);

        Call<UploadResult>call = api.uploadImage(requestFile) ;

        call.enqueue(new Callback<UploadResult>() {
            @Override
            public void onResponse(Call<UploadResult> call, Response<UploadResult> response) {


                if(response.code() == 200 && response.body() != null)
                {
                    UploadResult result = new UploadResult() ;
                    result = response.body() ;

                    // upload the data  to database
                    modelForProfile model = new modelForProfile(name, nickName, phone, emerph1, emerph2, area, uid,constants.DWLDURL +  result.getMsg(), emerph3 , Double.parseDouble("0") , Double.parseDouble("0"));

                    mref.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            savesomeData();


                        }
                    }) ;

                }

                else
                {
                    mprogressDialog.dismiss();

                    Toast.makeText(getApplicationContext() , "SomeThing Went Wrong Please  Try Again" , Toast.LENGTH_LONG)
                            .show();
                }


            }

            @Override
            public void onFailure(Call<UploadResult> call, Throwable t) {

              //  Log.d("RRR" , t.getMessage().toUpperCase().toString()) ;

                mprogressDialog.dismiss();

                Toast.makeText(getApplicationContext() , "SomeThing Went Wrong Please  Try Again" , Toast.LENGTH_LONG)
                        .show();
            }
        }) ;





    }



    private void BringImagePicker() {


        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setCropShape(CropImageView.CropShape.OVAL) //shaping the image
                .start(accountSetupPage.this);


    }

    @Override
    protected void onActivityResult(/*int requestCode, int resultCode, @Nullable Intent data*/
            int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mFilePathUri = result.getUri();


                proPicChooser.setImageURI(mFilePathUri);

                //sending data once  user select the image


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }



    private void savesomeData() {


        // save it on the off line
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("name", nickName);
        editor.putString("ph1", emerph1);
        editor.putString("ph2", emerph2);
        editor.putString("ph3", emerph3);
        editor.putString("pph", phone);
        editor.putString("uimage", userImagE);


        editor.apply();
        editor.commit();
        mprogressDialog.hide();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();


    }


    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = Objects.requireNonNull(cursor).getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;


    }




}
