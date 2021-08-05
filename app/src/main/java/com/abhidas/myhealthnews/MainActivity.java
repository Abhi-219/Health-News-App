package com.abhidas.myhealthnews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    boolean haveConnectedWifi = false;
    boolean haveConnectedMobile = false;
    int time = 0;

    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    EditText editText;
    Button button ;

    public int lastPosition;

    ArrayList<List_items> arr = new ArrayList<>();
    ArrayList<String> content = new ArrayList<>();

    //String myUrl = "https://saurav.tech/NewsAPI/top-headlines/category/health/in.json";
    // String myUrl = "https://newsapi.org/v2/top-headlines?q=health&apiKey=8c7cc42c761844ce9df12a37ede58939";
    String  myurl ="https://newsapi.org/v2/top-headlines?country=in&category=health&apiKey=8c7cc42c761844ce9df12a37ede58939";

    String result1 = "v2/top-headlines?country=in&category=";
    String result2 = "&apiKey=8c7cc42c761844ce9df12a37ede58939";
    String s="";
    String result="";
    int turn = 0;

    boolean connected = false;

    ConnectivityManager connectivityManager;
    ConnectivityManager.NetworkCallback nCallback;
    AlertDialog alertDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

//        button = (Button) findViewById(R.id.button);
        recyclerView = findViewById(R.id.recyclerView);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.myColor));
        }

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }

        if (!haveConnectedWifi && !haveConnectedMobile) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_Dialog_Alert);
            builder.setTitle("No internet Connection");
            builder.setMessage("Please turn on internet connection to continue");
            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            });
            builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAndRemoveTask();
                }
            });
            alertDialog = builder.create();
            alertDialog.show();
            Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_LONG).show();
        }


        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest request = new NetworkRequest.Builder().build();

        nCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onLost(@NonNull Network network) {
                time = 1;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_Dialog_Alert);
                builder.setTitle("No internet Connection");
                builder.setMessage("Please Turn On Internet  to continue");
                builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                });
                builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAndRemoveTask();
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();

            }

            @Override
            public void onAvailable(@NonNull Network network) {
                if (alertDialog != null)
                    alertDialog.dismiss();

                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if (time == 1) {
                            time = 0;
                            adapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "Reloaded ", Toast.LENGTH_SHORT).show();
                            bringData();
                        }


                    }
                });
            }

        };

        connectivityManager.registerNetworkCallback(request, nCallback);

        bringData();
    }


    public void bringData() {



        Methods methods = RetorfitClient.getRetrofitInstances().create(Methods.class);
        Call<Model> call = null;


            result+=result1+"health"+result2;
             call = methods.getAllData(result);
             turn=1;
             result="";
             s="";



        call.enqueue(new Callback<Model>() {

            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                Log.d("btn","223");
                ArrayList<Model.articles> articles = response.body().getArticles();

                for (Model.articles article : articles) {
                    arr.add(new List_items(article.getTitle(), article.getDescription(), article.getUrlToImage()));
                    content.add(article.getUrl());
                }

                final LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new RecyclerAdapter(MainActivity.this, arr, content);

                recyclerView.setAdapter(adapter);

                //retrieve last position on start
                SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                lastPosition = getPrefs.getInt("lastPos", 0);
                recyclerView.scrollToPosition(lastPosition);

                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        lastPosition = layoutManager.findFirstVisibleItemPosition();
                    }
                });

            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.d("Err", "Error");

            }
        });


    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStop() {
        connectivityManager.unregisterNetworkCallback(nCallback);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //save position in sharedpreferenses on destroy
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor e = getPrefs.edit();
        e.putInt("lastPos", lastPosition);
        e.apply();
    }

}

