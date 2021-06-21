package com.bu.safeguard;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bu.safeguard.models.modelForProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.katso.livebutton.LiveButton;

public class Profile extends AppCompatActivity {

    EditText fullName, nickName, mail, personalPhn, energencyPhn1, emergencyPhn2, emergencyph3, adress;
    ImageView personalEdit, emergencyEdit, editInPic;
    LiveButton updateButton;
    CircleImageView profileIMage;
    ProgressBar progressBar;
    String ImageLink = "", updatedLink = "null";
    Boolean isImageUploaded = false;
    StorageReference mStorageReference;
    ProgressDialog mprogressDialog;
    Uri mFilePathUri;
    DatabaseReference mref;
    private Bitmap compressedImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.drawable.bac);
        setContentView(R.layout.activity_profile);
        RelativeLayout relativeLayout = findViewById(R.id.peditLayout);
        fullName = (EditText) findViewById(R.id.fullName);
        nickName = (EditText) findViewById(R.id.nikName);
        mail = (EditText) findViewById(R.id.mail);
        profileIMage = findViewById(R.id.profile_image);
        personalPhn = (EditText) findViewById(R.id.personalPhn);
        energencyPhn1 = (EditText) findViewById(R.id.emergencyPhn1);
        emergencyPhn2 = (EditText) findViewById(R.id.emergencyPhn2);
        progressBar = findViewById(R.id.progrssBar);
        personalEdit = findViewById(R.id.personalInfoEdit);
        emergencyEdit = findViewById(R.id.emergenecyContactEdit);
        updateButton = findViewById(R.id.update);
        emergencyph3 = findViewById(R.id.emergencyPhn3);
        editInPic = findViewById(R.id.ppedit);

        adress = findViewById(R.id.area);

        ImageView bac = findViewById(R.id.backBtn);

        //pregress dialog
        mprogressDialog = new ProgressDialog(Profile.this);
        mStorageReference = FirebaseStorage.getInstance().getReference("ppUpdates");

        //  getSupportActionBar().hide();


        progressBar.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                progressBar.setVisibility(View.GONE);
            }
        }, 2000);

        fullName.setFocusable(false);
        nickName.setFocusable(false);
        mail.setFocusable(false);
        personalPhn.setFocusable(false);
        energencyPhn1.setFocusable(false);
        emergencyPhn2.setFocusable(false);
        adress.setFocusable(false);
        emergencyph3.setFocusable(false);

        fullName.setFocusableInTouchMode(false);
        nickName.setFocusableInTouchMode(false);
        mail.setFocusableInTouchMode(false);
        emergencyph3.setFocusableInTouchMode(false);
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

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                updateButton.setVisibility(View.VISIBLE);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(Profile.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(Profile.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                        BringImagePicker();


                    } else {

                        BringImagePicker();

                    }

                } else {

                    BringImagePicker();

                }


            }
        });

        personalEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButton.setVisibility(View.VISIBLE);
                fullName.setFocusable(true);
                nickName.setFocusable(true);
                adress.setFocusable(true);
                personalPhn.setFocusable(true);

                fullName.setFocusableInTouchMode(true);
                nickName.setFocusableInTouchMode(true);
                adress.setFocusableInTouchMode(true);
                personalPhn.setFocusableInTouchMode(true);

                fullName.setActivated(true);
                fullName.setPressed(true);
                int r = fullName.getText().length();
                fullName.setSelection(r);


            }
        });

        emergencyEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButton.setVisibility(View.VISIBLE);
                energencyPhn1.setFocusable(true);
                emergencyPhn2.setFocusable(true);
                emergencyph3.setFocusable(true);

                energencyPhn1.setFocusableInTouchMode(true);
                emergencyPhn2.setFocusableInTouchMode(true);
                emergencyph3.setFocusableInTouchMode(true);

                energencyPhn1.setActivated(true);
                energencyPhn1.setPressed(true);
                int r = energencyPhn1.getText().length();
                energencyPhn1.setSelection(r);

            }
        });


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // update every thing .
                updateButton.setVisibility(View.GONE);


                if (isConnected(Profile.this)) {
                    if (!ImageLink.isEmpty()) {
                        fullName.setFocusable(false);
                        nickName.setFocusable(false);
                        mail.setFocusable(false);
                        personalPhn.setFocusable(false);
                        energencyPhn1.setFocusable(false);
                        emergencyPhn2.setFocusable(false);
                        emergencyph3.setFocusable(false);
                        adress.setFocusable(false);

                        fullName.setFocusableInTouchMode(false);
                        nickName.setFocusableInTouchMode(false);
                        emergencyph3.setFocusableInTouchMode(false);
                        mail.setFocusableInTouchMode(false);
                        personalPhn.setFocusableInTouchMode(false);
                        energencyPhn1.setFocusableInTouchMode(false);
                        emergencyPhn2.setFocusableInTouchMode(false);
                        adress.setFocusableInTouchMode(false);

                        updateDataToFireBase();
                    } else {
                        fullName.setFocusable(false);
                        nickName.setFocusable(false);
                        mail.setFocusable(false);
                        personalPhn.setFocusable(false);
                        energencyPhn1.setFocusable(false);
                        emergencyPhn2.setFocusable(false);
                        emergencyph3.setFocusable(false);
                        adress.setFocusable(false);

                        fullName.setFocusableInTouchMode(false);
                        nickName.setFocusableInTouchMode(false);
                        emergencyph3.setFocusableInTouchMode(false);
                        mail.setFocusableInTouchMode(false);
                        personalPhn.setFocusableInTouchMode(false);
                        energencyPhn1.setFocusableInTouchMode(false);
                        emergencyPhn2.setFocusableInTouchMode(false);
                        adress.setFocusableInTouchMode(false);

                        Toast.makeText(getApplicationContext(), "You Are Not Conntected !", Toast.LENGTH_LONG)
                                .show();
                    }


                } else {
                    fullName.setFocusable(false);
                    nickName.setFocusable(false);
                    mail.setFocusable(false);
                    personalPhn.setFocusable(false);
                    energencyPhn1.setFocusable(false);
                    emergencyPhn2.setFocusable(false);
                    emergencyph3.setFocusable(false);
                    adress.setFocusable(false);

                    fullName.setFocusableInTouchMode(false);
                    nickName.setFocusableInTouchMode(false);
                    emergencyph3.setFocusableInTouchMode(false);
                    mail.setFocusableInTouchMode(false);
                    personalPhn.setFocusableInTouchMode(false);
                    energencyPhn1.setFocusableInTouchMode(false);
                    emergencyPhn2.setFocusableInTouchMode(false);
                    adress.setFocusableInTouchMode(false);

                    Toast.makeText(getApplicationContext(), "You Are Not Connected ", Toast.LENGTH_LONG)
                            .show();
                }

                //      updateDataToFireBase();

            }
        });

    }

    private boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            return (mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting());
        } else
            return false;

    }

    private void updateDataToFireBase() {
        progressBar.setVisibility(View.VISIBLE);
        String uid = FirebaseAuth.getInstance().getUid();

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("profile").child(uid);
        if (isImageUploaded) {

            if (!updatedLink.equals("null")) {
                Toast.makeText(getApplicationContext(), "Image Updated", Toast.LENGTH_LONG)
                        .show();

                mref.child("ppLink").setValue(updatedLink);
                ImageLink = updatedLink;
            }

        }

        modelForProfile model = new modelForProfile(fullName.getText().toString(), nickName.getText().toString(), personalPhn.getText().toString(), energencyPhn1.getText().toString(), emergencyPhn2.getText().toString(), adress.getText().toString(), uid, ImageLink, emergencyph3.getText().toString()
                , Double.parseDouble("0"),  Double.parseDouble("0"));


        mref.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                //String nickName, String emerph1, String emerph2 , String userimage;
                Toast.makeText(getApplicationContext(), "Profile Data Updated", Toast.LENGTH_LONG)
                        .show();

                saveTheseForOffline(nickName.getText().toString(), energencyPhn1.getText().toString(), emergencyPhn2.getText().toString(), ImageLink, emergencyph3.getText().toString(), personalPhn.getText().toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void downloadData() {
        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("profile").child(uid);

        mref.keepSynced(true);


        {
            try {

                mref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            modelForProfile model = dataSnapshot.getValue(modelForProfile.class);
                            Picasso.get().load(model.getPpLink()).error(R.drawable.user).placeholder(R.drawable.user).into(profileIMage);
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

                        //   saveTheseForOffline(model.getNickName() , model.getEmerph1()  , model.emerph2 , ImageLink) ;

                        else {
                            Toast.makeText(getApplicationContext(), "Could not Find The Profile .", Toast.LENGTH_SHORT)
                                    .show();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        //   getdata();
                    }
                });


                // Toast.makeText(getApplicationContext(), "name : " + mail  , Toast.LENGTH_SHORT)
                //    .show();
            } catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(), "Error  : ", Toast.LENGTH_SHORT)
                        .show();
            }

        }


        //  getdata();
        // Toast.makeText(getApplicationContext() , "You Are Not Conntected " , Toast.LENGTH_LONG)
        //  .show();


    }

    @Override
    protected void onStart() {
        super.onStart();
        downloadData();


    }

    private void saveTheseForOffline(String nickName, String emerph1, String emerph2, String userimage, String s, String toString) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("name", nickName);
        editor.putString("ph1", emerph1);
        editor.putString("ph2", emerph2);
        editor.putString("ph3", s);
        editor.putString("pph", toString);
        editor.putString("uimage", userimage);


        editor.apply();
        editor.commit();
        // downloadData();


    }

    private void BringImagePicker() {


        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setCropShape(CropImageView.CropShape.OVAL) //shaping the image
                .start(Profile.this);


    }

    @Override
    protected void onActivityResult(/*int requestCode, int resultCode, @Nullable Intent data*/
            int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mFilePathUri = result.getUri();

                profileIMage.setImageURI(mFilePathUri);
                //    uploadPicToServer(mFilePathUri) ;

                uploadProfilePicToServer(mFilePathUri);

                //sending data once  user select the image

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    private void uploadProfilePicToServer(Uri mFilePathUri) {

        profileIMage.setImageURI(mFilePathUri);
        mprogressDialog.show();
        mprogressDialog.setMessage("Uploading New Image");

        // call for network

        File file = new File(mFilePathUri.getPath());

        File compressed;

        try {
            compressed = new Compressor(this)
                    .setMaxHeight(600)
                    .setMaxWidth(600)
                    .setQuality(50)
                    .compressToFile(file);
        } catch (Exception e) {
            compressed = file;
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

        Call<UploadResult> call = api.uploadImage(requestFile);

        call.enqueue(new Callback<UploadResult>() {
            @Override
            public void onResponse(Call<UploadResult> call, Response<UploadResult> response) {


                if (response.code() == 200 && response.body() != null) {
                    UploadResult result = response.body();
                    updatedLink = constants.DWLDURL + result.getMsg().toString();
                    ImageLink = constants.DWLDURL + result.getMsg().toString();
                    isImageUploaded = true;
                    mprogressDialog.dismiss();
                } else {
                    mprogressDialog.dismiss();
                    isImageUploaded = false;
                    Toast.makeText(getApplicationContext(), "SomeThing Went Wrong. Please  Try Again !", Toast.LENGTH_LONG)
                            .show();
                }

            }

            @Override
            public void onFailure(Call<UploadResult> call, Throwable t) {
                Log.d("RRR", t.getMessage().toUpperCase().toString());

                mprogressDialog.dismiss();
                isImageUploaded = false;
                Toast.makeText(getApplicationContext(), "SomeTHing Went Wrong Please  Try Again", Toast.LENGTH_LONG)
                        .show();
            }
        });


    }

    private void uploadPicToServer(Uri mFilePathUri) {

        if (mFilePathUri != null) {
            final String randomName = UUID.randomUUID().toString();

            // PHOTO UPLOAD
            File newImageFile = new File(mFilePathUri.getPath());

//             try {
//
////                 compressedImageFile = new Compressor(Profile.this)
////                         .setMaxHeight(900)
////                         .setMaxWidth(900)
////                         .setQuality(50)
////                         .compressToBitmap(newImageFile);
//
//             } catch (IOException e) {
//                 e.printStackTrace();
//             }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] imageData = baos.toByteArray();
            UploadTask filePath = mStorageReference.child(randomName + ".jpg").putBytes(imageData);

            filePath.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    Uri downloaduri = uriTask.getResult();

                    updatedLink = downloaduri.toString();

                    isImageUploaded = true;


                    mprogressDialog.hide();

                    // mref.child(ts).setValue(downloaduri.toString());


                    //  sentToPrevActivity();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mprogressDialog.hide();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                    mprogressDialog.setMessage("Uploading.......");
                    mprogressDialog.show();


                }
            });


        } else {

            Toast.makeText(this, "Please Pick an Image to Update", Toast.LENGTH_LONG).show();

        }


    }

}
