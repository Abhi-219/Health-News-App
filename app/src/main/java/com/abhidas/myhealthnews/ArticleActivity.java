package com.abhidas.myhealthnews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.monstertechno.adblocker.AdBlockerWebView;
import com.monstertechno.adblocker.util.AdBlocker;

public class ArticleActivity extends AppCompatActivity {
    boolean connected=false;

    ConnectivityManager connectivityManager;
    ConnectivityManager.NetworkCallback  nCallback;
    AlertDialog alertDialog;

    Intent intent;
    WebView webView;
    String load="";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

       // getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.myColor));
        }

        connectivityManager= (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest request = new NetworkRequest.Builder().build();

        nCallback = new ConnectivityManager.NetworkCallback(){
            @Override
            public void onLost(@NonNull Network network) {
                AlertDialog.Builder builder =new AlertDialog.Builder(ArticleActivity.this,R.style.Theme_AppCompat_Dialog_Alert);
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

            }

            @Override
            public void onAvailable(@NonNull Network network) {
                if(alertDialog!=null)
                    alertDialog.dismiss();
                  //   webView.reload();
            }

        };

        connectivityManager.registerNetworkCallback(request,nCallback);

        webView = findViewById(R.id.webView);

        new AdBlockerWebView.init(this).initializeWebView(webView);   // AD Block
        webView.setWebViewClient(new Browser_home());                         // AD Block

         intent = getIntent();
         webView.loadUrl(intent.getStringExtra("content"));
         load = intent.getStringExtra("content");
//        Log.i("URL","A2 37");
         webView.loadUrl(load);
//        Log.i("URL","A2 39");
    }

    // AD Block --->
    public class Browser_home extends WebViewClient {
        Browser_home() {}
        @SuppressWarnings("deprecation")
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            return AdBlockerWebView.blockAds(view,url) ? AdBlocker.createEmptyResource() :
                    super.shouldInterceptRequest(view, url);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.article_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.back:
                if(webView.canGoBack()){
                    webView.goBack();
                } else {
                    onBackPressed();
                }
                break;
            case R.id.refresh:
                webView.reload();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(ArticleActivity.this, MainActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
        startActivity(myIntent);
        finish();
        return;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStop() {
        connectivityManager.unregisterNetworkCallback(nCallback);
        super.onStop();
    }
}