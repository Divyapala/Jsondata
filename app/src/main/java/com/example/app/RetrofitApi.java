package com.example.app;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface RetrofitApi {
    @GET("photos")
    Call<List<Network>> getUsers();
}
