package com.ruff.game_lesson_1.levels;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.ruff.game_lesson_1.GameLevels;
import com.ruff.game_lesson_1.LivesSingleton;
import com.ruff.game_lesson_1.MyInterstitialAd;
import com.ruff.game_lesson_1.MyMediaPlayer;
import com.ruff.game_lesson_1.MyRewardedAd;
import com.ruff.game_lesson_1.R;
import com.ruff.game_lesson_1.databinding.UniversalBinding;

import java.util.Random;


public class Level15 extends AppCompatActivity {

    private UniversalBinding binding;
    Slider slider;
    Animation animation;
    private int leftNumCard;
    int rightNumCard;

    int[] flyAnimalArray;
    String[] flyAnimalTextArray;
    int[] notFlyAnimalArray;
    String[] notFlyAnimalTextArray;
    int order;
    MyMediaPlayer soundEndDialog, soundLivesDialog;
    LivesSingleton livesSingleton;
    private MyInterstitialAd myInterstitialAd;
    private MyRewardedAd myRewardedAd;
    private final int wrongAnswerPoint = 2;
    private final int trueAnswerPoint = 1;
    private final int oneLive = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UniversalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window w = getWindow();
        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //Скрываем нижнюю панель навигации.
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); //Появляется поверх игры и исчезает.

        //Запрещаем ночную тему.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        livesSingleton = LivesSingleton.getInstance();

        //проверка на бесконечные жизни (покупка инапа в приложении) - начало
        if (livesSingleton.isEndlessLives()) {
            binding.tvHeartCounter.setText("∞");
        } else {
            binding.tvHeartCounter.setText(String.valueOf(livesSingleton.getCurrentLives()));
        }

        //Проверка на премиум доступ
        if (livesSingleton.isEndlessLives()) { //если есть премиум
            myInterstitialAd = null;
        } else { //если нет премиума
            //загрузка межстраничной рекламы в память
            myInterstitialAd = MyInterstitialAd.getInstance();
            myInterstitialAd.loadInterstitialAd(this);

            //загрузка рекламы с вознаграждением в память
            myRewardedAd = MyRewardedAd.getInstance();
            myRewardedAd.loadRewardedAd(this);
        }

        soundEndDialog = new MyMediaPlayer(this, R.raw.sound_level_complete);
        soundLivesDialog = new MyMediaPlayer(this, R.raw.sound_level_fail);

        flyAnimalArray = new int[]{R.drawable.im_bat_animal, R.drawable.im_butterfly, R.drawable.im_flying_squirrel,
                R.drawable.im_peacock, R.drawable.im_eagle, R.drawable.im_owl, R.drawable.im_goose,
                R.drawable.im_hen, R.drawable.im_partridge, R.drawable.im_flying_fish, R.drawable.im_chafer,
                R.drawable.im_ladybug, R.drawable.im_mantis, R.drawable.im_rhinoceros_beetle};

        notFlyAnimalArray = new int[]{R.drawable.im_gold_fish, R.drawable.im_kiwi_animal, R.drawable.im_ostrich,
                R.drawable.im_penguin, R.drawable.im_squirrel, R.drawable.im_ant, R.drawable.im_spider,
                R.drawable.im_snail, R.drawable.im_platypus, R.drawable.im_chameleon, R.drawable.im_lizard,
                R.drawable.im_grasshopper};


        //фон заднего экрана
        binding.myUniversalConstraint.setBackgroundResource(R.drawable.im_back_level15);

        //кнопка назад
        binding.btBack.setOnClickListener(v -> {
            onBackPressed();
        });


        //установка номера уровня
        binding.tvLevelNumber.setText(R.string.level_15);


        //скругление углов картинок
        binding.tvLeftNumber.setClipToOutline(true);
        binding.tvRightNumber.setClipToOutline(true);

        //вызов диалогового окна
        initStartDialog();

        //инициализация цифр на карточках
        initCards();
    }

    private void initStartDialog() {

        Dialog dialogStart = new Dialog(this);
        dialogStart.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogStart.setContentView(R.layout.preview_dialog);

        Window w = dialogStart.getWindow();
        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //Скрываем нижнюю панель навигации.
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); //Появляется поверх игры и исчезает.

        ConstraintLayout constraintLayout = dialogStart.findViewById(R.id.my_preview_dialog_constraint);
        constraintLayout.setBackgroundResource(R.drawable.im_back_dialog_level15);
        ImageView ivDialog = dialogStart.findViewById(R.id.imageView);
        ivDialog.setImageResource(R.drawable.two_cards_level15);
        TextView tvDescription = dialogStart.findViewById(R.id.textView);
        tvDescription.setText(getResources().getString(R.string.exercise_level15));
        dialogStart.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogStart.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialogStart.setCancelable(false);
        dialogStart.show();

        MaterialButton close = dialogStart.findViewById(R.id.bt_close_dialog);
        close.setOnClickListener(v -> {
            onBackPressed();
        });

        MaterialButton _continue = dialogStart.findViewById(R.id.bt_continue);
        _continue.setOnClickListener(v -> {
            dialogStart.cancel();
        });


    }

    private void initEndDialog() {
        soundEndDialog.play();
        Dialog dialogEnd = new Dialog(this);
        dialogEnd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEnd.setContentView(R.layout.end_dialog);

        Window w = dialogEnd.getWindow();
        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //Скрываем нижнюю панель навигации.
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); //Появляется поверх игры и исчезает.

        ConstraintLayout constraintLayout = dialogEnd.findViewById(R.id.my_end_dialog_constraint);
        constraintLayout.setBackgroundResource(R.drawable.im_back_dialog_level15);
        dialogEnd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvTextDialogEnd = dialogEnd.findViewById(R.id.textView);
        tvTextDialogEnd.setText(getResources().getString(R.string.interesting_fact_level15));
        dialogEnd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialogEnd.setCancelable(false);
        dialogEnd.show();

        MaterialButton close = dialogEnd.findViewById(R.id.bt_close_dialog);
        close.setOnClickListener(v -> {
            soundEndDialog.stopPlay();

            //показ рекламы
            if (!livesSingleton.isEndlessLives() && myInterstitialAd.getLevelCompleteCounter() == myInterstitialAd.getMaxLevelComplete()) {
                myInterstitialAd.showInterstitialAd(null, Level15.this);
                myInterstitialAd.setLevelCompleteCounter(0);
            } else {
                onBackPressed();
            }
        });

        MaterialButton _continue = dialogEnd.findViewById(R.id.bt_continue);
        _continue.setOnClickListener(v -> {
            soundEndDialog.stopPlay();
            dialogEnd.cancel();
            Intent intent = new Intent(Level15.this, Level16.class);

            if (!livesSingleton.isEndlessLives() && myInterstitialAd.getLevelCompleteCounter() == myInterstitialAd.getMaxLevelComplete()) {
                myInterstitialAd.showInterstitialAd(intent, Level15.this);
                myInterstitialAd.setLevelCompleteCounter(0);
            } else {
                startActivity(intent);
                finish();
            }
        });

    }

    private void initLivesDialog() {
        soundLivesDialog.play();
        Dialog dialogLives = new Dialog(this);
        dialogLives.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLives.setContentView(R.layout.lives_dialog);

        Window w = dialogLives.getWindow();
        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //Скрываем нижнюю панель навигации.
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); //Появляется поверх игры и исчезает.

        dialogLives.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogLives.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialogLives.setCancelable(false);
        dialogLives.show();

        MaterialButton close = dialogLives.findViewById(R.id.bt_close_dialog);
        close.setOnClickListener(v -> {
            soundLivesDialog.stopPlay();
            onBackPressed();
        });

        MaterialButton restore = dialogLives.findViewById(R.id.bt_restore);
        restore.setOnClickListener(v -> {
            soundLivesDialog.stopPlay();
            //TODO реализовать просмотр рекламы c вознаграждением
            myRewardedAd.showRewardedAd(Level15.this, binding.tvHeartCounter);
            dialogLives.cancel();
            myRewardedAd.loadRewardedAd(this);
        });
    }


    @SuppressLint("ClickableViewAccessibility")
    private void initCards() {

        animation = AnimationUtils.loadAnimation(this, R.anim.my_animation);
        slider = binding.slider;

        initCardViews();

        binding.tvLeftNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    binding.tvRightNumber.setEnabled(false);
                    if (order > 0) {
                        binding.tvLeftNumber.setBackground(getDrawable(R.drawable.im_back_true));
                    } else {
                        binding.tvLeftNumber.setBackground(getDrawable(R.drawable.im_back_wrong));
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (order > 0) {
                        if (slider.getValue() < slider.getValueTo()) {
                            slider.setValue(slider.getValue() + trueAnswerPoint);
                        }
                    } else {
                        //проверка на премиум доступ - начало
                        if (!livesSingleton.isEndlessLives()) { //если бесконечных жизней нет

                            //проверка на оставшиеся жизни - начало
                            if (livesSingleton.getCurrentLives() > 0) {
                                livesSingleton.setCurrentLives(livesSingleton.getCurrentLives() - oneLive); //вычитание жизни из текущего количества жизней
                                binding.tvHeartCounter.setText(String.valueOf(livesSingleton.getCurrentLives()));

                                //условие при окончании жизней - начало
                                if (livesSingleton.getCurrentLives() == 0) {
                                    initLivesDialog(); //вызов диалога конца жизней
                                }
                                // //условие при окончании жизней - конец
                            }
                            //проверка на оставшиеся жизни - конец
                        }
                        //проверка на премиум доступ - конец

                        if (slider.getValue() > 1) {
                            slider.setValue(slider.getValue() - wrongAnswerPoint);
                        } else if (slider.getValue() == 1) {
                            slider.setValue(0);
                        }
                    }

                    if (slider.getValue() == slider.getValueTo()) {
                        if (!livesSingleton.isEndlessLives()) {
                            myInterstitialAd.setLevelCompleteCounter(myInterstitialAd.getLevelCompleteCounter() + 1); //наращивание счетчика завершенных уровневней для показа рекламы
                        }
                        initEndDialog();
                    } else {
                        binding.tvLeftNumber.startAnimation(animation);
                        initCardViews();

                    }

                    binding.tvRightNumber.setEnabled(true);
                }

                return true;
            }
        });


        binding.tvRightNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    binding.tvLeftNumber.setEnabled(false);
                    if (order <= 0) {
                        binding.tvRightNumber.setBackground(getDrawable(R.drawable.im_back_true));
                    } else {
                        binding.tvRightNumber.setBackground(getDrawable(R.drawable.im_back_wrong));
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (order <= 0) {
                        if (slider.getValue() < slider.getValueTo()) {
                            slider.setValue(slider.getValue() + trueAnswerPoint);
                        }
                    } else {
                        //проверка на премиум доступ - начало
                        if (!livesSingleton.isEndlessLives()) { //если бесконечных жизней нет

                            //проверка на оставшиеся жизни - начало
                            if (livesSingleton.getCurrentLives() > 0) {
                                livesSingleton.setCurrentLives(livesSingleton.getCurrentLives() - oneLive); //вычитание жизни из текущего количества жизней
                                binding.tvHeartCounter.setText(String.valueOf(livesSingleton.getCurrentLives()));

                                //условие при окончании жизней - начало
                                if (livesSingleton.getCurrentLives() == 0) {
                                    initLivesDialog(); //вызов диалога конца жизней
                                }
                                // //условие при окончании жизней - конец
                            }
                            //проверка на оставшиеся жизни - конец
                        }
                        //проверка на премиум доступ - конец

                        if (slider.getValue() > 1) {
                            slider.setValue(slider.getValue() - wrongAnswerPoint);
                        } else if (slider.getValue() == 1) {
                            slider.setValue(0);
                        }
                    }

                    if (slider.getValue() == slider.getValueTo()) {
                        if (!livesSingleton.isEndlessLives()) {
                            myInterstitialAd.setLevelCompleteCounter(myInterstitialAd.getLevelCompleteCounter() + 1); //наращивание счетчика завершенных уровневней для показа рекламы
                        }
                        initEndDialog();
                    } else {
                        binding.tvRightNumber.startAnimation(animation);
                        initCardViews();
                    }

                    binding.tvLeftNumber.setEnabled(true);
                }

                return true;
            }
        });


    }


    public void initCardViews() {

        flyAnimalTextArray = getResources().getStringArray(R.array.fly_animal_array);
        notFlyAnimalTextArray = getResources().getStringArray(R.array.not_fly_animal_array);


        Random random = new Random();
        order = random.nextInt(2);
        leftNumCard = random.nextInt(flyAnimalArray.length);
        rightNumCard = random.nextInt(notFlyAnimalArray.length);

        if (order > 0) {
            binding.tvLeftNumber.setBackgroundResource(flyAnimalArray[leftNumCard]);
            binding.tvRightNumber.setBackgroundResource(notFlyAnimalArray[rightNumCard]);
            binding.tvLeftNumberText.setText(flyAnimalTextArray[leftNumCard]);
            binding.tvRightNumberText.setText(notFlyAnimalTextArray[rightNumCard]);
        } else {
            binding.tvRightNumber.setBackgroundResource(flyAnimalArray[leftNumCard]);
            binding.tvLeftNumber.setBackgroundResource(notFlyAnimalArray[rightNumCard]);
            binding.tvRightNumberText.setText(flyAnimalTextArray[leftNumCard]);
            binding.tvLeftNumberText.setText(notFlyAnimalTextArray[rightNumCard]);
        }
    }

    @Override
    public void onBackPressed() {
        soundEndDialog.stopPlay();
        soundLivesDialog.stopPlay();
        Intent intent = new Intent(Level15.this, GameLevels.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundEndDialog.isPlaying()) {
            soundEndDialog.stopPlay();
        }

        if (soundLivesDialog.isPlaying()) {
            soundLivesDialog.stopPlay();
        }
    }

}