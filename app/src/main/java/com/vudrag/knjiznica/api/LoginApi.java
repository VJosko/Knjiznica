package com.vudrag.knjiznica.api;

import com.vudrag.knjiznica.dataObjects.AuthToken;
import com.vudrag.knjiznica.dataObjects.LoginData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginApi {

    @POST("api/authenticate")
    Call<AuthToken> login(@Body LoginData loginData);
}
