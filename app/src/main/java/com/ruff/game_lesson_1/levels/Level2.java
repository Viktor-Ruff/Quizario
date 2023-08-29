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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.ruff.game_lesson_1.GameLevels;
import com.ruff.game_lesson_1.R;
import com.ruff.game_lesson_1.databinding.UniversalBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Level2 extends AppCompatActivity {

    private UniversalBinding binding;
    private static final int COUNT_QUESTIONS = 10;
    Slider slider;
    Animation animation;
    Map<Integer, String> romanNumArray;
    int leftNumCard;
    int rightNumCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UniversalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        int screenLayout = getBaseContext().getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;


        if (screenLayout == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            binding.tvLeftNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 48);
            binding.tvRightNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 48);
        } else if (screenLayout == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            binding.tvLeftNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 68);
            binding.tvRightNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 68);
        } else if (screenLayout == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            binding.tvLeftNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 108);
            binding.tvRightNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 108);
        } else if (screenLayout == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            binding.tvLeftNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 138);
            binding.tvRightNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 138);
        }
        //кнопка назад
        binding.btBack.setOnClickListener(v -> {
            onBackPressed();
        });


        //установка номера уровня
        binding.tvLevelNumber.setText(R.string.level_2);


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
        ImageView ivDialog = dialogStart.findViewById(R.id.imageView);
        ivDialog.setImageResource(R.drawable.im_roman_two_cards);

        TextView tvDescription = dialogStart.findViewById(R.id.textView);
        tvDescription.setText(getResources().getString(R.string.exercise_level2));
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
        Dialog dialogEnd = new Dialog(this);
        dialogEnd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEnd.setContentView(R.layout.end_dialog);
        dialogEnd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogEnd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialogEnd.setCancelable(false);
        dialogEnd.show();

        MaterialButton close = dialogEnd.findViewById(R.id.bt_close_dialog);
        close.setOnClickListener(v -> {
            onBackPressed();
        });

        MaterialButton _continue = dialogEnd.findViewById(R.id.bt_continue);
        _continue.setOnClickListener(v -> {
            dialogEnd.cancel();

            Intent intent = new Intent(Level2.this, Level3.class);
            startActivity(intent);
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
                        if (slider.getValue() < COUNT_QUESTIONS) {
                            slider.setValue(slider.getValue() + 1);
                        }
                    } else {
                        if (slider.getValue() > 0) {
                            slider.setValue(slider.getValue() - 1);
                        }
                    }

                    if (slider.getValue() == COUNT_QUESTIONS) {
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
                        if (slider.getValue() < COUNT_QUESTIONS) {
                            slider.setValue(slider.getValue() + 1);
                        }
                    } else {
                        if (slider.getValue() > 0) {
                            slider.setValue(slider.getValue() - 1);
                        }
                    }

                    if (slider.getValue() == COUNT_QUESTIONS) {
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
        Intent intent = new Intent(Level2.this, GameLevels.class);
        startActivity(intent);
    }

}