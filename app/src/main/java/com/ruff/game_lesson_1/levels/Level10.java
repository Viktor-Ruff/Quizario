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


public class Level10 extends AppCompatActivity {

    private UniversalBinding binding;
    Slider slider;
    Animation animation;
    private int leftNumCard;
    int rightNumCard;

    int[] metalArray;
    String[] metalTextArray;
    int[] notMetalArray;
    String[] notMetalTextArray;
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

        //загрузка рекламы в память
        myInterstitialAd = MyInterstitialAd.getInstance();
        myInterstitialAd.loadInterstitialAd(this);

        //загрузка рекламы с вознаграждением в память
        myRewardedAd = MyRewardedAd.getInstance();
        myRewardedAd.loadRewardedAd(this);

        livesSingleton = LivesSingleton.getInstance();
        binding.tvHeartCounter.setText(String.valueOf(livesSingleton.getCurrentLives()));

        soundEndDialog = new MyMediaPlayer(this, R.raw.sound_level_complete);
        soundLivesDialog = new MyMediaPlayer(this, R.raw.sound_level_fail);

        metalArray = new int[]{R.drawable.im_chain, R.drawable.im_container, R.drawable.im_dumbbell,
                R.drawable.im_dumbbell_o, R.drawable.im_key, R.drawable.im_lock, R.drawable.im_safe,
                R.drawable.im_ring, R.drawable.im_scissors, R.drawable.im_compass};

        notMetalArray = new int[]{R.drawable.im_ball_f, R.drawable.im_ball_b, R.drawable.im_bat,
                R.drawable.im_mouse, R.drawable.im_pouch, R.drawable.im_rope, R.drawable.im_shoes,
                R.drawable.im_belt, R.drawable.im_racket, R.drawable.im_skis};


        //фон заднего экрана
        binding.myUniversalConstraint.setBackgroundResource(R.drawable.im_back_level10);

        //кнопка назад
        binding.btBack.setOnClickListener(v -> {
            onBackPressed();
        });


        //установка номера уровня
        binding.tvLevelNumber.setText(R.string.level_10);


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
        ConstraintLayout constraintLayout = dialogStart.findViewById(R.id.my_preview_dialog_constraint);
        constraintLayout.setBackgroundResource(R.drawable.im_back_dialog_level10);
        ImageView ivDialog = dialogStart.findViewById(R.id.imageView);
        ivDialog.setImageResource(R.drawable.two_cards_level10);
        TextView tvDescription = dialogStart.findViewById(R.id.textView);
        tvDescription.setText(getResources().getString(R.string.exercise_level10));
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
        ConstraintLayout constraintLayout = dialogEnd.findViewById(R.id.my_end_dialog_constraint);
        constraintLayout.setBackgroundResource(R.drawable.im_back_dialog_level10);
        dialogEnd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvTextDialogEnd = dialogEnd.findViewById(R.id.textView);
        tvTextDialogEnd.setText(getResources().getString(R.string.interesting_fact_level10));
        dialogEnd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialogEnd.setCancelable(false);
        dialogEnd.show();

        MaterialButton close = dialogEnd.findViewById(R.id.bt_close_dialog);
        close.setOnClickListener(v -> {
            soundEndDialog.stopPlay();

            //показ рекламы
            if (myInterstitialAd.getLevelCompleteCounter() == myInterstitialAd.getMaxLevelComplete()) {
                myInterstitialAd.showInterstitialAd(null, Level10.this);
                myInterstitialAd.setLevelCompleteCounter(0);
            } else {
                onBackPressed();
            }
        });

        MaterialButton _continue = dialogEnd.findViewById(R.id.bt_continue);
        _continue.setOnClickListener(v -> {
            soundEndDialog.stopPlay();
            dialogEnd.cancel();
            Intent intent = new Intent(Level10.this, Level11.class);

            if (myInterstitialAd.getLevelCompleteCounter() == myInterstitialAd.getMaxLevelComplete()) {
                myInterstitialAd.showInterstitialAd(intent, Level10.this);
                myInterstitialAd.setLevelCompleteCounter(0);
            } else {
                startActivity(intent);
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
            onBackPressed();
        });

        MaterialButton restore = dialogLives.findViewById(R.id.bt_restore);
        restore.setOnClickListener(v -> {
            soundLivesDialog.stopPlay();
            //TODO реализовать просмотр рекламы c вознаграждением
            myRewardedAd.showRewardedAd(Level10.this, binding.tvHeartCounter);
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
                        if (livesSingleton.getCurrentLives() > 0) {
                            livesSingleton.setCurrentLives(livesSingleton.getCurrentLives() - oneLive);
                            binding.tvHeartCounter.setText(String.valueOf(livesSingleton.getCurrentLives()));
                            if (livesSingleton.getCurrentLives() == 0) {
                                initLivesDialog();
                            }
                        }

                        if (slider.getValue() > 1) {
                            slider.setValue(slider.getValue() - wrongAnswerPoint);
                        } else if (slider.getValue() == 1) {
                            slider.setValue(0);
                        }
                    }

                    if (slider.getValue() == slider.getValueTo()) {
                        myInterstitialAd.setLevelCompleteCounter(myInterstitialAd.getLevelCompleteCounter() + 1);
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
                        if (livesSingleton.getCurrentLives() > 0) {
                            livesSingleton.setCurrentLives(livesSingleton.getCurrentLives() - oneLive);
                            binding.tvHeartCounter.setText(String.valueOf(livesSingleton.getCurrentLives()));
                            if (livesSingleton.getCurrentLives() == 0) {
                                initLivesDialog();
                            }
                        }

                        if (slider.getValue() > 1) {
                            slider.setValue(slider.getValue() - wrongAnswerPoint);
                        } else if (slider.getValue() == 1) {
                            slider.setValue(0);
                        }
                    }

                    if (slider.getValue() == slider.getValueTo()) {
                        myInterstitialAd.setLevelCompleteCounter(myInterstitialAd.getLevelCompleteCounter() + 1);
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

        metalTextArray = getResources().getStringArray(R.array.metal_array);
        notMetalTextArray = getResources().getStringArray(R.array.not_metal_array);


        Random random = new Random();
        order = random.nextInt(2);
        leftNumCard = random.nextInt(metalArray.length);
        rightNumCard = random.nextInt(notMetalArray.length);

        if (order > 0) {
            binding.tvLeftNumber.setBackgroundResource(metalArray[leftNumCard]);
            binding.tvRightNumber.setBackgroundResource(notMetalArray[rightNumCard]);
            binding.tvLeftNumberText.setText(metalTextArray[leftNumCard]);
            binding.tvRightNumberText.setText(notMetalTextArray[rightNumCard]);
        } else {
            binding.tvRightNumber.setBackgroundResource(metalArray[leftNumCard]);
            binding.tvLeftNumber.setBackgroundResource(notMetalArray[rightNumCard]);
            binding.tvRightNumberText.setText(metalTextArray[leftNumCard]);
            binding.tvLeftNumberText.setText(notMetalTextArray[rightNumCard]);
        }
    }

    @Override
    public void onBackPressed() {
        soundEndDialog.stopPlay();
        soundLivesDialog.stopPlay();
        Intent intent = new Intent(Level10.this, GameLevels.class);
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