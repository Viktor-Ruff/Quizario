package com.ruff.game_lesson_1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;


/**
 * Created by Viktor-Ruff
 * Date: 03.09.2023
 * Time: 18:44
 */
public class MyRewardedAd {

    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
    private static MyRewardedAd instance;
    private static final String TAG = "MyRewardedAd";
    private RewardedAd rewardedAd;

    private MyRewardedAd() {
    }

    public static MyRewardedAd getInstance() {
        if (instance == null) {
            instance = new MyRewardedAd();
        }

        return instance;
    }


    public void loadRewardedAd(Context context) {
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();


        RewardedAd.load(context, AD_UNIT_ID,
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.toString());
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        Log.d(TAG, "Ad was loaded.");
                    }
                });


    }

    public void showRewardedAd(Activity activity, TextView textView) {
        if (rewardedAd != null) {

            rewardedAd.show(activity, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d(TAG, "The user earned the reward.");
                    LivesSingleton livesSingleton = LivesSingleton.getInstance();
                    livesSingleton.setCurrentLives(livesSingleton.getMaxLives());
                    textView.setText(String.valueOf(livesSingleton.getCurrentLives()));
                }
            });
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }

        rewardedAdCallBack();
    }

    private void rewardedAdCallBack() {
        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
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
                //rewardedAd = null;
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.");
                rewardedAd = null;
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


}
