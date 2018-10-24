package com.expertily.lawsome.API.services;

import com.expertily.lawsome.API.models.responses.OTPRes;
import com.expertily.lawsome.API.models.responses.VerifyRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OTPInterface {

    @GET("sendotp.php")
    Call<OTPRes> sendOTP(@Query("authkey") String authkey, @Query("mobile") String mobile, @Query("message") String message, @Query("sender") String sender, @Query("otp") String otp);

    @GET("verifyRequestOTP.php")
    Call<VerifyRes> verifyOTP(@Query("authkey") String authkey, @Query("mobile") String mobile, @Query("otp") String otp);

}
