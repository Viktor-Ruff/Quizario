package com.ruff.game_lesson_1;

import static com.ruff.game_lesson_1.MainActivity.MY_LOG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.button.MaterialButton;
import com.ruff.game_lesson_1.databinding.ActivityGameLevelsBinding;
import com.ruff.game_lesson_1.levels.Level1;
import com.ruff.game_lesson_1.levels.Level2;
import com.ruff.game_lesson_1.levels.Level3;
import com.ruff.game_lesson_1.levels.Level4;
import com.ruff.game_lesson_1.levels.Level5;

public class GameLevels extends AppCompatActivity {

    private ActivityGameLevelsBinding binding;

    private LivesSingleton livesSingleton;

    MyMediaPlayer soundLivesDialog;
    private MyRewardedAd myRewardedAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameLevelsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        soundLivesDialog = new MyMediaPlayer(this, R.raw.sound_level_fail);
        myRewardedAd = MyRewardedAd.getInstance();
        myRewardedAd.loadRewardedAd(this);
        livesSingleton = LivesSingleton.getInstance();
        binding.tvHeartCounter.setText(String.valueOf(livesSingleton.getCurrentLives()));


        levelListeners();

        //click handling button back
        binding.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(GameLevels.this, MainActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            }
        });

    }

    private void levelListeners() {


        //enter to level 1
        binding.tvLevel1.setOnClickListener(v -> {

            if (livesSingleton.getCurrentLives() > 0) {
                try {
                    Intent intent = new Intent(GameLevels.this, Level1.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            } else {
                initLivesDialog();
            }


        });

        //enter to level 2
        binding.tvLevel2.setOnClickListener(v -> {

            if (livesSingleton.getCurrentLives() > 0) {
                try {
                    Intent intent = new Intent(GameLevels.this, Level2.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            } else {
                initLivesDialog();
            }
        });


        //enter to level 3
        binding.tvLevel3.setOnClickListener(v -> {

            if (livesSingleton.getCurrentLives() > 0) {
                try {
                    Intent intent = new Intent(GameLevels.this, Level3.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            } else {
                initLivesDialog();
            }

        });

        //enter to level 4
        binding.tvLevel4.setOnClickListener(v -> {

            if (livesSingleton.getCurrentLives() > 0) {
                try {
                    Intent intent = new Intent(GameLevels.this, Level4.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            } else {
                initLivesDialog();
            }
        });

        //enter to level 5
        binding.tvLevel5.setOnClickListener(v -> {

            if (livesSingleton.getCurrentLives() > 0) {
                try {
                    Intent intent = new Intent(GameLevels.this, Level5.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            } else {
                initLivesDialog();
            }
        });
    }

    private void initLivesDialog() {
        soundLivesDialog.play();
        Dialog dialogLives = new Dialog(this);
        dialogLives.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLives.setContentView(R.layout.lives_dialog);
        dialogLives.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogLives.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialogLives.setCancelable(false);
        dialogLives.show();

        MaterialButton close = dialogLives.findViewById(R.id.bt_close_dialog);
        close.setOnClickListener(v -> {
            soundLivesDialog.stopPlay();
            dialogLives.cancel();
        });

        MaterialButton restore = dialogLives.findViewById(R.id.bt_restore);
        restore.setOnClickListener(v -> {
            soundLivesDialog.stopPlay();
            myRewardedAd.showRewardedAd(GameLevels.this, binding.tvHeartCounter);
            dialogLives.cancel();
            myRewardedAd.loadRewardedAd(this);
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GameLevels.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

