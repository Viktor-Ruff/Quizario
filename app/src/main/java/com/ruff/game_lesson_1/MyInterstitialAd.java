package com.ruff.game_lesson_1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;


/**
 * Created by Viktor-Ruff
 * Date: 03.09.2023
 * Time: 14:05
 */
public class MyInterstitialAd {

    private static MyInterstitialAd instance;
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";
    private static final String TAG = "MyInterstitialAd";
    private InterstitialAd mInterstitialAd;
    private int levelCompleteCounter = 0;
    private int maxLevelComplete = 2;


    private MyInterstitialAd() {
    }

    public static MyInterstitialAd getInstance() {
        if (instance == null) {
            instance = new MyInterstitialAd();
        }

        return instance;
    }


    public void loadInterstitialAd(Context context) {
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(context, AD_UNIT_ID, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }

    public void showInterstitialAd(Intent intent, Activity activity) {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(activity);
            initialAdCallBack(intent, activity);

        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }


    private void initialAdCallBack(Intent intent, Activity activity) {
        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad dismissed fullscreen content.");
                mInterstitialAd = null;
                if (intent == null) {
                    activity.onBackPressed();
                } else {
                    activity.startActivity(intent);
                }

            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.");
                mInterstitialAd = null;
                if (intent == null) {
                    activity.onBackPressed();
                } else {
                    activity.startActivity(intent);
                }
            }

            @Override
            public void onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.");
            }

        });
    }


    public int getLevelCompleteCounter() {
        return levelCompleteCounter;
    }

    public void setLevelCompleteCounter(int levelCompleteCounter) {
        this.levelCompleteCounter = levelCompleteCounter;
    }

    public int getMaxLevelComplete() {
        return maxLevelComplete;
    }
}
