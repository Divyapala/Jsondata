package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private RetrofitAdapter customAdapter;
    private ArrayList<Network> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getUserListFromRestApi();

    }

    private void getUserListFromRestApi() {

        progressDialog = createProgressDialog(MainActivity.this);

        RetrofitApi retrofitInterface = ApiClient.getClient().create(RetrofitApi.class);


        Call<List<Network>> call = retrofitInterface.getUsers();
        call.enqueue(new Callback<List<Network>>() {
            @Override
            public void onResponse(Call<List<Network>> call, Response<List<Network>> response) {

                progressDialog.dismiss();
                userList = new ArrayList<>(response.body());
                customAdapter = new RetrofitAdapter(getApplicationContext(), userList, new CustomItemClickListener() {
                    @Override
                    public void onItemClick(Network user, int position) {

                        Toast.makeText(getApplicationContext(),""+user.getTitle(),Toast.LENGTH_SHORT).show();

                    }
                });
                recyclerView.setAdapter(customAdapter);

            }

            @Override
            public void onFailure(Call<List<Network>> call, Throwable t) {
                progressDialog.dismiss();
                DialogHelper.getAlertWithMessage("fail",t.getMessage(),MainActivity.this);
            }
        });


    }


    public ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_layout);
        return dialog;
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.search);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapter.getFilter().filter(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}