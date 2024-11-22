package com.example.projectfinal.API;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface API {
    @FormUrlEncoded
    @POST("auth/sign_up")
    Call<ResponseBody> createUser(
            @Field("firstname") String firstName,
            @Field("lastname") String lastName,
            @Field("email") String email,
            @Field("sdt") String sdt,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("auth/log_in")
    Call<ResponseBody> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("auth/forget_password")
    Call<ResponseBody> forgotpassword(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("auth/verify_code")
    Call<ResponseBody> verifycode(
            @Field("code") String code
    );

    @FormUrlEncoded
    @POST("auth/change_forget")
    Call<ResponseBody> resetpassword(
            @Field("newpassword") String password
    );
}
