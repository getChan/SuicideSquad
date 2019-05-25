package com.example.skarn.please;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ImageInterface {
    @Multipart
    @POST("process/image")
    Call<ResultObject> uploadImageToServer(@Part MultipartBody.Part image);
}