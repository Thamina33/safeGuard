package com.bu.safeguard;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;


public class IntroActivity  extends AppIntro {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Note here that we DO NOT use setContentView();
        getSupportActionBar().hide();
        // Add your slide fragments here.
        setBarColor(Color.parseColor("#008577"));
        // AppIntro will automatically generate the dots indicator and buttons.
        Dexter.withContext(IntroActivity.this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE ,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE ,
                        Manifest.permission.ACCESS_FINE_LOCATION ,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                        permissionToken.continuePermissionRequest();
                    }
                }).onSameThread().check(); ;

        addSlide(new FristFragment());
        addSlide(new SecondFragment());
        addSlide(new thridFragment());
        addSlide(new ForthFragment());


        // Instead of fragments, you can also use our default slide.
        // Just create a `SliderPage` and provide title, description, background and image.
        // AppIntro will do the rest.
//        SliderPage sliderPage = new SliderPage();
//        sliderPage.setTitle("বাকসৈনিক");
//        sliderPage.setDescription("বাংলাদেশের কন্যা সন্তানদের\n" +
//                "নিরাপত্তা নিশ্চিত করতে\n" +
//                "জনগণের নিরাপত্তা প্রহরী ‘ বাকসৈনিক ’");
//        sliderPage.setImageDrawable(R.drawable.bak_logo);
//        sliderPage.setBgColor(R.color.white);
//        addSlide(AppIntroFragment.newInstance(sliderPage));
//
//        SliderPage sliderPage1 = new SliderPage();
//        sliderPage1.setTitle("সাহস সংক্রামক");
//        sliderPage1.setDescription("কন্যাদের নিরাপত্তায় আপনি এগিয়ে আসুন\n" +
//                "আপনার বিপদেও এগিয়ে আসবে ‘বাকসৈনিক’");
//        sliderPage1.setImageDrawable(R.drawable.bak_logo);
//        sliderPage1.setBgColor(R.color.white);
//        addSlide(AppIntroFragment.newInstance(sliderPage1));


//        SliderPage sliderPage2 = new SliderPage();
//        sliderPage2.setTitle("");
//        sliderPage2.setDescription("সামাজিক অংশগ্রহণের মাধ্যমে আমাদের\n" +
//                "নিরাপত্তা নিশ্চিত করতে ‘বাকসৈনিকে’ আপনার \n" +
//                "অংশগ্রহণ একান্ত কাম্য।");
//        sliderPage2.setImageDrawable(R.drawable.bak_logo);
//        sliderPage2.setBgColor(R.color.white);
//        addSlide(AppIntroFragment.newInstance(sliderPage2));
//
//        SliderPage sliderPage3 = new SliderPage();
//        sliderPage3.setTitle("");
//        sliderPage3.setDescription("ভবিষ্যৎ সন্তানের জন্য মানবিক, আদর্শ সমাজ গঠনে" +
//                "‘ বাকসৈনিক ’ \n আপনার সাথে যুক্ত হয়ে কাজ করবে");
//        sliderPage3.setImageDrawable(R.drawable.bak_logo);
//        sliderPage3.setBgColor(R.color.white);
//        addSlide(AppIntroFragment.newInstance(sliderPage3));


        // OPTIONAL METHODS
        // Override bar/separator color.

//        setBarColor(Color.parseColor("#3F51B5"));
//        setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(true);
       // setButtonsEnabled(false);


        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        //setVibrate(true);
      //  setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.

        Intent i = new Intent(getApplicationContext()  , login_activity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.

        Intent i = new Intent(getApplicationContext()  , login_activity.class);
        startActivity(i);
        finish();

    }



    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.

        setFlowAnimation();
    }
    }



