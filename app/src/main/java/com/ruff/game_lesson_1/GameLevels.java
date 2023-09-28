package com.ruff.game_lesson_1;

import static com.ruff.game_lesson_1.MainActivity.MY_LOG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.ruff.game_lesson_1.databinding.ActivityGameLevelsBinding;
import com.ruff.game_lesson_1.levels.Level1;
import com.ruff.game_lesson_1.levels.Level10;
import com.ruff.game_lesson_1.levels.Level11;
import com.ruff.game_lesson_1.levels.Level12;
import com.ruff.game_lesson_1.levels.Level13;
import com.ruff.game_lesson_1.levels.Level14;
import com.ruff.game_lesson_1.levels.Level15;
import com.ruff.game_lesson_1.levels.Level16;
import com.ruff.game_lesson_1.levels.Level17;
import com.ruff.game_lesson_1.levels.Level18;
import com.ruff.game_lesson_1.levels.Level19;
import com.ruff.game_lesson_1.levels.Level2;
import com.ruff.game_lesson_1.levels.Level20;
import com.ruff.game_lesson_1.levels.Level3;
import com.ruff.game_lesson_1.levels.Level4;
import com.ruff.game_lesson_1.levels.Level5;
import com.ruff.game_lesson_1.levels.Level6;
import com.ruff.game_lesson_1.levels.Level7;
import com.ruff.game_lesson_1.levels.Level8;
import com.ruff.game_lesson_1.levels.Level9;

public class GameLevels extends AppCompatActivity {


    private static final String SAVE_FILE = "SAVE_FILE";
    private static final String LEVEL_KEY = "LEVEL_KEY";


    private int levelCounter;
    SharedPreferences getProgress;


    private ActivityGameLevelsBinding binding;

    private LivesSingleton livesSingleton;

    MyMediaPlayer soundLivesDialog;
    private MyRewardedAd myRewardedAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameLevelsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getProgress = getSharedPreferences(SAVE_FILE, MODE_PRIVATE);
        levelCounter = getProgress.getInt(LEVEL_KEY, 1);
        /*levelCounter = 19;
        SharedPreferences.Editor editor = getProgress.edit();
        editor.putInt(LEVEL_KEY, levelCounter);
        editor.apply();*/

        Window w = getWindow();
        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //Скрываем нижнюю панель навигации.
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); //Появляется поверх игры и исчезает.

        //Запрещаем ночную тему.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //плеер для проигрывания звука окончания жизней
        soundLivesDialog = new MyMediaPlayer(this, R.raw.sound_level_fail);

        //инициализация рекламы с вознаграждением
        myRewardedAd = MyRewardedAd.getInstance();
        myRewardedAd.loadRewardedAd(this);

        //инициализация синглтона жизней
        livesSingleton = LivesSingleton.getInstance(this);

        //проверка на бесконечные жизни (покупка инапа в приложении)
        if (livesSingleton.isEndlessLives()) {
            binding.tvHeartCounter.setText("∞");
        } else {
            binding.tvHeartCounter.setText(String.valueOf(livesSingleton.getCurrentLives()));
        }

        //инициализация слушателей нажатий на кнопки уровней
        levelListeners();

        //обработка нажатий на кнопку назад
        binding.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    Intent intent = new Intent(GameLevels.this, MainActivity.class);
                    startActivity(intent); //переход на мэйн активити
                    finish(); //уничтожение активности
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            }
        });


        int[] openedLevels = {R.id.tv_level_1, R.id.tv_level_2, R.id.tv_level_3, R.id.tv_level_4, R.id.tv_level_5,
                R.id.tv_level_6, R.id.tv_level_7, R.id.tv_level_8, R.id.tv_level_9, R.id.tv_level_10, R.id.tv_level_11,
                R.id.tv_level_12, R.id.tv_level_13, R.id.tv_level_14, R.id.tv_level_15, R.id.tv_level_16, R.id.tv_level_17,
                R.id.tv_level_18, R.id.tv_level_19, R.id.tv_level_20};

        for (int i = 1; i < levelCounter; i++) {
            TextView tv = findViewById(openedLevels[i]);
            tv.setText(String.valueOf(i + 1));
        }

    }

    private void levelListeners() {

        //enter to level 1
        binding.tvLevel1.setOnClickListener(v -> {

            if (livesSingleton.getCurrentLives() > 0 || livesSingleton.isEndlessLives()) {
                try {

                    if (levelCounter >= 1) {
                        Intent intent = new Intent(GameLevels.this, Level1.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            } else {
                initLivesDialog();
            }


        });

        //enter to level 2
        binding.tvLevel2.setOnClickListener(v -> {

            if (livesSingleton.getCurrentLives() > 0 || livesSingleton.isEndlessLives()) {
                try {
                    if (levelCounter >= 2) {
                        Intent intent = new Intent(GameLevels.this, Level2.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            } else {
                initLivesDialog();
            }
        });


        //enter to level 3
        binding.tvLevel3.setOnClickListener(v -> {

            if (livesSingleton.getCurrentLives() > 0 || livesSingleton.isEndlessLives()) {
                try {
                    if (levelCounter >= 3) {
                        Intent intent = new Intent(GameLevels.this, Level3.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            } else {
                initLivesDialog();
            }

        });

        //enter to level 4
        binding.tvLevel4.setOnClickListener(v -> {

            if (livesSingleton.getCurrentLives() > 0 || livesSingleton.isEndlessLives()) {
                try {
                    if (levelCounter >= 4) {
                        Intent intent = new Intent(GameLevels.this, Level4.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            } else {
                initLivesDialog();
            }
        });

        //enter to level 5
        binding.tvLevel5.setOnClickListener(v -> {

            if (livesSingleton.getCurrentLives() > 0 || livesSingleton.isEndlessLives()) {
                try {
                    if (levelCounter >= 5) {
                        Intent intent = new Intent(GameLevels.this, Level5.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            } else {
                initLivesDialog();
            }
        });


        //enter to level 6
        binding.tvLevel6.setOnClickListener(v -> {

            if (livesSingleton.getCurrentLives() > 0 || livesSingleton.isEndlessLives()) {
                try {
                    if (levelCounter >= 6) {
                        Intent intent = new Intent(GameLevels.this, Level6.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            } else {
                initLivesDialog();
            }
        });


        //enter to level 7
        binding.tvLevel7.setOnClickListener(v -> {

            if (livesSingleton.getCurrentLives() > 0 || livesSingleton.isEndlessLives()) {
                try {
                    if (levelCounter >= 7) {
                        Intent intent = new Intent(GameLevels.this, Level7.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            } else {
                initLivesDialog();
            }
        });


        //enter to level 8
        binding.tvLevel8.setOnClickListener(v -> {

            if (livesSingleton.getCurrentLives() > 0 || livesSingleton.isEndlessLives()) {
                try {
                    if (levelCounter >= 8) {
                        Intent intent = new Intent(GameLevels.this, Level8.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            } else {
                initLivesDialog();
            }
        });


        //enter to level 9
        binding.tvLevel9.setOnClickListener(v -> {

            if (livesSingleton.getCurrentLives() > 0 || livesSingleton.isEndlessLives()) {
                try {
                    if (levelCounter >= 9) {
                        Intent intent = new Intent(GameLevels.this, Level9.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            } else {
                initLivesDialog();
            }
        });

        //enter to level 10
        binding.tvLevel10.setOnClickListener(v -> {

            if (livesSingleton.getCurrentLives() > 0 || livesSingleton.isEndlessLives()) {
                try {
                    if (levelCounter >= 10) {
                        Intent intent = new Intent(GameLevels.this, Level10.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            } else {
                initLivesDialog();
            }
        });


        //enter to level 11
        binding.tvLevel11.setOnClickListener(v -> {

            if (livesSingleton.getCurrentLives() > 0 || livesSingleton.isEndlessLives()) {
                try {
                    if (levelCounter >= 11) {
                        Intent intent = new Intent(GameLevels.this, Level11.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            } else {
                initLivesDialog();
            }
        });

        //enter to level 12
        binding.tvLevel12.setOnClickListener(v -> {

            if (livesSingleton.getCurrentLives() > 0 || livesSingleton.isEndlessLives()) {
                try {
                    if (levelCounter >= 12) {
                        Intent intent = new Intent(GameLevels.this, Level12.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            } else {
                initLivesDialog();
            }
        });


        //enter to level 13
        binding.tvLevel13.setOnClickListener(v -> {

            if (livesSingleton.getCurrentLives() > 0 || livesSingleton.isEndlessLives()) {
                try {
                    if (levelCounter >= 13) {
                        Intent intent = new Intent(GameLevels.this, Level13.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            } else {
                initLivesDialog();
            }
        });

        //enter to level 14
        binding.tvLevel14.setOnClickListener(v -> {

            if (livesSingleton.getCurrentLives() > 0 || livesSingleton.isEndlessLives()) {
                try {
                    if (levelCounter >= 14) {
                        Intent intent = new Intent(GameLevels.this, Level14.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            } else {
                initLivesDialog();
            }
        });

        //enter to level 15
        binding.tvLevel15.setOnClickListener(v -> {

            if (livesSingleton.getCurrentLives() > 0 || livesSingleton.isEndlessLives()) {
                try {
                    if (levelCounter >= 15) {
                        Intent intent = new Intent(GameLevels.this, Level15.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            } else {
                initLivesDialog();
            }
        });


        //enter to level 16
        binding.tvLevel16.setOnClickListener(v -> {

            if (livesSingleton.getCurrentLives() > 0 || livesSingleton.isEndlessLives()) {
                try {
                    if (levelCounter >= 16) {
                        Intent intent = new Intent(GameLevels.this, Level16.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            } else {
                initLivesDialog();
            }
        });


        //enter to level 17
        binding.tvLevel17.setOnClickListener(v -> {

            if (livesSingleton.getCurrentLives() > 0 || livesSingleton.isEndlessLives()) {
                try {
                    if (levelCounter >= 17) {
                        Intent intent = new Intent(GameLevels.this, Level17.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            } else {
                initLivesDialog();
            }
        });

        //enter to level 18
        binding.tvLevel18.setOnClickListener(v -> {

            if (livesSingleton.getCurrentLives() > 0 || livesSingleton.isEndlessLives()) {
                try {
                    if (levelCounter >= 18) {
                        Intent intent = new Intent(GameLevels.this, Level18.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            } else {
                initLivesDialog();
            }
        });

        //enter to level 19
        binding.tvLevel19.setOnClickListener(v -> {

            if (livesSingleton.getCurrentLives() > 0 || livesSingleton.isEndlessLives()) {
                try {
                    if (levelCounter >= 19) {
                        Intent intent = new Intent(GameLevels.this, Level19.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            } else {
                initLivesDialog();
            }
        });


        //enter to level 20
        binding.tvLevel20.setOnClickListener(v -> {

            if (livesSingleton.getCurrentLives() > 0 || livesSingleton.isEndlessLives()) {
                try {
                    if (levelCounter >= 20) {
                        Intent intent = new Intent(GameLevels.this, Level20.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            } else {
                initLivesDialog();
            }
        });
    }


    private void initLivesDialog() {
        soundLivesDialog.play(); //проигрывание звука окончания жизней
        Dialog dialogLives = new Dialog(this);
        dialogLives.requestWindowFeature(Window.FEATURE_NO_TITLE); //диалог без названия
        dialogLives.setContentView(R.layout.lives_dialog); //выбор макета для диалога

        Window w = dialogLives.getWindow();
        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //Скрываем нижнюю панель навигации.
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); //Появляется поверх игры и исчезает.

        dialogLives.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //затемнение заднего фона диалога
        dialogLives.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT); //растягивания диалога на экране
        dialogLives.setCancelable(false); //отключение системной кнопки назад
        dialogLives.show(); //отображение диалога

        //обработка кнопки закрыть (крестик) - начало.
        MaterialButton close = dialogLives.findViewById(R.id.bt_close_dialog);
        close.setOnClickListener(v -> {
            soundLivesDialog.stopPlay();
            dialogLives.cancel(); //закрытие диалогового окна
        });
        //обработка кнопки закрыть (крестик) - конец.

        //обработка кнопки продолжить - начало
        MaterialButton restore = dialogLives.findViewById(R.id.bt_restore);
        restore.setOnClickListener(v -> {
            soundLivesDialog.stopPlay();
            myRewardedAd.showRewardedAd(GameLevels.this, binding.tvHeartCounter); //просмотр рекламы c вознаграждением
            dialogLives.cancel(); //закрытие диалогового окна
            myRewardedAd.loadRewardedAd(this); //загрузка в фоне новой рекламы

        });
        //обработка кнопки продолжить - конец

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GameLevels.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

