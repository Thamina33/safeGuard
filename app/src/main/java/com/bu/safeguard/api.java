package com.bu.safeguard;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface api {
    @Multipart
    @POST("upload.php")
    Call<UploadResult> uploadImage(@Part("image\"; filename=\"myfile.jpg\" ") RequestBody file);


}
