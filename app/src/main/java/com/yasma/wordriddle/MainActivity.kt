package com.yasma.wordriddle

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.Image
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class MainActivity : AppCompatActivity() {
    private var mInterstitialAd: InterstitialAd? = null
    private final var TAG = "MainActivity"
    var  count=1;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_WordRiddle)
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this) {}
        val play: Button = findViewById(R.id.button2)
        val rule:Button=findViewById(R.id.button3)

        play.setOnClickListener()
        {
            check();
        }

        rule.setOnClickListener()
        {
            val intent = Intent(this, Rules::class.java)
            // start your next activity
            startActivity(intent)

        }




    }
 private  fun loadad()
 {
     var adRequest = AdRequest.Builder().build()

     InterstitialAd.load(this,"ca-app-pub-6065082492952635/8199782004", adRequest, object : InterstitialAdLoadCallback() {
         override fun onAdFailedToLoad(adError: LoadAdError) {
             Log.d(TAG, "$adError")
             mInterstitialAd = null
         }

         override fun onAdLoaded(interstitialAd: InterstitialAd) {
             Log.d(TAG, "Ad was loaded.")
             mInterstitialAd = interstitialAd
         }
     })
 }
    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
  private  fun start()
  {

      val webVie =findViewById<View>(R.id.web) as WebView
      webVie.setBackgroundColor(Color.TRANSPARENT)
      webVie.loadUrl("https://zakirhusain-3802.github.io/Zakirword/")
      val webSetting = webVie.settings
      webSetting.javaScriptEnabled=true
      webVie.webViewClient= WebViewClient()
      webVie.setScrollContainer(false);
      webVie.setVerticalScrollBarEnabled(false); // Disable vertical scroll bar
      webVie.setHorizontalScrollBarEnabled(false);

      webVie.canGoBack()
      webVie.setOnKeyListener(View.OnKeyListener OnkeyListener@{ v, keyCode, event->
          if  (keyCode==KeyEvent.KEYCODE_BACK
              && event.action == MotionEvent.ACTION_UP
              && webVie.canGoBack()) {
              webVie.goBack()
              return@OnkeyListener true
          }

          false


      })
      val checkButton: Button = findViewById(R.id.button)
      val rl:Button=findViewById(R.id.button4)

      rl.setOnClickListener()
      {

          val we:WebView=findViewById(R.id.web)
          val r:ImageView=findViewById(R.id.imageView6)
          val b:Button=findViewById(R.id.button)
          val back:Button=findViewById(R.id.button5)
          val stats:Button=findViewById(R.id.stats)
          if(we.isVisible)
          {
              we.setVisibility(View.GONE);
              b.setVisibility(View.GONE);
              r.setVisibility(View.VISIBLE);
              rl.setVisibility(View.GONE);
              back.setVisibility(View.VISIBLE);
              stats.visibility=View.INVISIBLE

          }
         back.setOnClickListener()
         {
             we.setVisibility(View.VISIBLE);
             b.setVisibility(View.VISIBLE);
             r.setVisibility(View.GONE);
             rl.setVisibility(View.VISIBLE);
             back.setVisibility(View.INVISIBLE);
             stats.visibility=View.INVISIBLE
         }

//          else
//          {
//              we.setVisibility(View.VISIBLE);
//              b.setVisibility(View.VISIBLE);
//              r.setVisibility(View.GONE);
//          }

      }
      checkButton.setOnClickListener()
      {
          var a =count %3
          if(a==0)
          {
              if (mInterstitialAd != null) {
                  mInterstitialAd?.show(this)
              } else {
                  Log.d("TAG", "The interstitial ad wasn't ready yet.")
              }
              loadad();

          }
          start();
          Toast.makeText(this, "New Game Started", Toast.LENGTH_SHORT).show()
          count=count+1;
      }
      lateinit var mAdView : AdView
      mAdView = findViewById(R.id.adView)
      val adRequest = AdRequest.Builder().build()
      mAdView.loadAd(adRequest)
  }

    public  fun check()
    {
//        val checkint=findViewById<View>(R.id.web) as WebView
        if (checkForInternet(this)) {
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
            setContentView(R.layout.nointernet);
            start();
            loadad();



        } else {
            Toast.makeText(this, "PLz Turn On Internet", Toast.LENGTH_SHORT).show()
//            checkint.setVisibility(View.VISIBLE);
//            setContentView(R.layout.nointernet);
        }
    }


}