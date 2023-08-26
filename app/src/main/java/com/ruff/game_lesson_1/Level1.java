package com.ruff.game_lesson_1;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.ruff.game_lesson_1.databinding.UniversalBinding;

import java.util.Random;


public class Level1 extends AppCompatActivity {

    private UniversalBinding binding;
    private static final int COUNT_QUESTIONS = 10;
    Slider slider;

    Animation animation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UniversalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //кнопка назад
        binding.btBack.setOnClickListener(v -> {
            onBackPressed();
        });

        //установка номера уровня
        binding.tvLevelNumber.setText(R.string.level_1);

        //скругление углов картинок
        binding.tvLeftNumber.setClipToOutline(true);
        binding.tvRightNumber.setClipToOutline(true);

        //вызов диалогового окна
        initDialog();

        //инициализация цифр на карточках
        initCards();
    }

    private void initDialog() {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.preview_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        MaterialButton close = dialog.findViewById(R.id.bt_close_dialog);
        close.setOnClickListener(v -> {
            onBackPressed();
        });

        MaterialButton _continue = dialog.findViewById(R.id.bt_continue);
        _continue.setOnClickListener(v -> {
            dialog.cancel();
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
                    if (Integer.parseInt(binding.tvLeftNumber.getText().toString()) > Integer.parseInt(binding.tvRightNumber.getText().toString())) {
                        //TODO написать смену картинки на галочку
                        binding.tvLeftNumber.setBackground(getDrawable(R.drawable.tv_style_green_40));
                    } else {
                        //TODO написать смену картинки на крестик
                        binding.tvLeftNumber.setBackground(getDrawable(R.drawable.tv_style_red_40));
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (Integer.parseInt(binding.tvLeftNumber.getText().toString()) > Integer.parseInt(binding.tvRightNumber.getText().toString())) {
                        if (slider.getValue() < COUNT_QUESTIONS) {
                            slider.setValue(slider.getValue() + 1);
                        }
                    } else {
                        if (slider.getValue() > 0) {
                            slider.setValue(slider.getValue() - 1);
                        }
                    }

                    if (slider.getValue() == COUNT_QUESTIONS) {
                        //TODO выполнить выход из уровня.
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
                    if (Integer.parseInt(binding.tvRightNumber.getText().toString()) > Integer.parseInt(binding.tvLeftNumber.getText().toString())) {
                        //TODO написать смену картинки на галочку
                        binding.tvRightNumber.setBackground(getDrawable(R.drawable.tv_style_green_40));
                    } else {
                        //TODO написать смену картинки на крестик
                        binding.tvRightNumber.setBackground(getDrawable(R.drawable.tv_style_red_40));
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (Integer.parseInt(binding.tvRightNumber.getText().toString()) > Integer.parseInt(binding.tvLeftNumber.getText().toString())) {
                        if (slider.getValue() < COUNT_QUESTIONS) {
                            slider.setValue(slider.getValue() + 1);
                        }
                    } else {
                        if (slider.getValue() > 0) {
                            slider.setValue(slider.getValue() - 1);
                        }
                    }

                    if (slider.getValue() == COUNT_QUESTIONS) {
                        //TODO выполнить выход из уровня.
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

        String[] textArray = getResources().getStringArray(R.array.textArray);
        Random random = new Random();
        int left = random.nextInt(textArray.length);
        int right = random.nextInt(textArray.length);

        while (left == right) {
            right = random.nextInt(textArray.length);
        }

        binding.tvLeftNumber.setText(String.valueOf(left));
        binding.tvRightNumber.setText(String.valueOf(right));
        binding.tvLeftNumberText.setText(textArray[left]);
        binding.tvRightNumberText.setText(textArray[right]);
        binding.tvLeftNumber.setBackground(getDrawable(R.drawable.tv_style_white_40));
        binding.tvRightNumber.setBackground(getDrawable(R.drawable.tv_style_white_40));
    }

}