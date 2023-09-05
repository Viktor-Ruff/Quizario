package com.ruff.game_lesson_1.levels;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Level11 extends AppCompatActivity {

    private UniversalBinding binding;
    Slider slider;
    Animation animation;
    Map<Integer, String> romanNumArray;
    int leftNumCard;
    int rightNumCard;

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

        int screenLayout = getBaseContext().getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;


        if (screenLayout == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            binding.tvLeftNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 62);
            binding.tvRightNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 62);
        } else if (screenLayout == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            binding.tvLeftNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 84);
            binding.tvRightNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 84);
        } else if (screenLayout == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            binding.tvLeftNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 122);
            binding.tvRightNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 122);
        } else if (screenLayout == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            binding.tvLeftNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 152);
            binding.tvRightNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 152);
        }
        //кнопка назад
        binding.btBack.setOnClickListener(v -> {
            onBackPressed();
        });


        //установка номера уровня
        binding.tvLevelNumber.setText(R.string.level_11);


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
        constraintLayout.setBackgroundResource(R.drawable.im_back_dialog_preview);
        ImageView ivDialog = dialogStart.findViewById(R.id.imageView);
        ivDialog.setImageResource(R.drawable.two_cards_level11);
        TextView tvDescription = dialogStart.findViewById(R.id.textView);
        tvDescription.setText(getResources().getString(R.string.exercise_level11));
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
        constraintLayout.setBackgroundResource(R.drawable.im_back_dialog_preview);
        dialogEnd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvTextDialogEnd = dialogEnd.findViewById(R.id.textView);
        tvTextDialogEnd.setText(getResources().getString(R.string.interesting_fact_level11));
        dialogEnd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialogEnd.setCancelable(false);
        dialogEnd.show();

        MaterialButton close = dialogEnd.findViewById(R.id.bt_close_dialog);
        close.setOnClickListener(v -> {
            soundEndDialog.stopPlay();

            //показ рекламы
            if (myInterstitialAd.getLevelCompleteCounter() == myInterstitialAd.getMaxLevelComplete()) {
                myInterstitialAd.showInterstitialAd(null, Level11.this);
                myInterstitialAd.setLevelCompleteCounter(0);
            } else {
                onBackPressed();
            }
        });

        MaterialButton _continue = dialogEnd.findViewById(R.id.bt_continue);
        _continue.setOnClickListener(v -> {
            soundEndDialog.stopPlay();
            dialogEnd.cancel();
            Intent intent = new Intent(Level11.this, Level3.class);
            if (myInterstitialAd.getLevelCompleteCounter() == myInterstitialAd.getMaxLevelComplete()) {
                myInterstitialAd.showInterstitialAd(intent, Level11.this);
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
            myRewardedAd.showRewardedAd(Level11.this, binding.tvHeartCounter);
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
                    if (leftNumCard > rightNumCard) {
                        binding.tvLeftNumber.setBackground(getDrawable(R.drawable.tv_style_green_40));
                    } else {
                        binding.tvLeftNumber.setBackground(getDrawable(R.drawable.tv_style_red_40));
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (leftNumCard > rightNumCard) {
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
                    if (rightNumCard > leftNumCard) {
                        binding.tvRightNumber.setBackground(getDrawable(R.drawable.tv_style_green_40));
                    } else {
                        binding.tvRightNumber.setBackground(getDrawable(R.drawable.tv_style_red_40));
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (rightNumCard > leftNumCard) {
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


        romanNumArray = new HashMap<>();

        romanNumArray.put(1, "I");
        romanNumArray.put(2, "II");
        romanNumArray.put(3, "III");
        romanNumArray.put(4, "IV");
        romanNumArray.put(5, "V");
        romanNumArray.put(6, "VI");
        romanNumArray.put(7, "VII");
        romanNumArray.put(8, "VIII");
        romanNumArray.put(9, "IX");
        romanNumArray.put(10, "X");

        String[] textArray = getResources().getStringArray(R.array.textArray);
        Random random = new Random();
        leftNumCard = random.nextInt(textArray.length);
        rightNumCard = random.nextInt(textArray.length);

        while (leftNumCard == rightNumCard) {
            rightNumCard = random.nextInt(textArray.length);
        }

        binding.tvLeftNumber.setText(romanNumArray.get(leftNumCard + 1));
        binding.tvRightNumber.setText(String.valueOf(romanNumArray.get(rightNumCard + 1)));
        binding.tvLeftNumberText.setText(String.valueOf(romanNumArray.get(leftNumCard + 1)));
        binding.tvRightNumberText.setText(String.valueOf(romanNumArray.get(rightNumCard + 1)));
        binding.tvLeftNumber.setBackground(getDrawable(R.drawable.tv_style_white_40));
        binding.tvRightNumber.setBackground(getDrawable(R.drawable.tv_style_white_40));
    }

    @Override
    public void onBackPressed() {
        soundEndDialog.stopPlay();
        soundLivesDialog.stopPlay();
        Intent intent = new Intent(Level11.this, GameLevels.class);
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