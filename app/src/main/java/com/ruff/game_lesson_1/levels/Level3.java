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
import com.ruff.game_lesson_1.R;
import com.ruff.game_lesson_1.databinding.UniversalBinding;

import java.util.Random;


public class Level3 extends AppCompatActivity {

    private UniversalBinding binding;
    private static final int COUNT_QUESTIONS = 10;
    Slider slider;
    Animation animation;
    private int leftNumCard;
    int rightNumCard;

    int[] predatorArray;
    String[] predatorTextArray;
    int[] herbivorousArray;
    String[] herbivorousTextArray;

    int order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UniversalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        predatorArray = new int[]{R.drawable.im_bear, R.drawable.im_wolf, R.drawable.im_fox,
                R.drawable.im_leon, R.drawable.im_pantera};
        herbivorousArray = new int[]{R.drawable.im_cow, R.drawable.im_camel, R.drawable.im_elephant,
                R.drawable.im_squirrel, R.drawable.im_pig};


        //фон заднего экрана
        binding.myUniversalConstraint.setBackgroundResource(R.drawable.im_back_level3);

        //кнопка назад
        binding.btBack.setOnClickListener(v -> {
            onBackPressed();
        });


        //установка номера уровня
        binding.tvLevelNumber.setText(R.string.level_3);


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
        ivDialog.setImageResource(R.drawable.two_cards_level3);

        TextView tvDescription = dialogStart.findViewById(R.id.textView);
        tvDescription.setText(getResources().getString(R.string.exercise_level3));
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
        dialogEnd.setContentView(R.layout.preview_dialog);
        ImageView ivDialog = dialogEnd.findViewById(R.id.imageView);
        ivDialog.setImageResource(R.drawable.two_cards_level3);

        TextView tvDescription = dialogEnd.findViewById(R.id.textView);
        tvDescription.setText(getResources().getString(R.string.interesting_fact_level3));
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

            /*Intent intent = new Intent(Level2.this, Level3.class);
            startActivity(intent);*/
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
                    if (order <= 0) {
                        binding.tvRightNumber.setBackground(getDrawable(R.drawable.im_back_true));
                    } else {
                        binding.tvRightNumber.setBackground(getDrawable(R.drawable.im_back_wrong));
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (order <= 0) {
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

        predatorTextArray = getResources().getStringArray(R.array.animalsPredatorArray);
        herbivorousTextArray = getResources().getStringArray(R.array.animalsHerbivorousArray);


        Random random = new Random();
        order = random.nextInt(2);
        leftNumCard = random.nextInt(predatorArray.length);
        rightNumCard = random.nextInt(herbivorousArray.length);

        if (order > 0) {
            binding.tvLeftNumber.setBackgroundResource(predatorArray[leftNumCard]);
            binding.tvRightNumber.setBackgroundResource(herbivorousArray[rightNumCard]);
            binding.tvLeftNumberText.setText(predatorTextArray[leftNumCard]);
            binding.tvRightNumberText.setText(herbivorousTextArray[rightNumCard]);
        } else {
            binding.tvRightNumber.setBackgroundResource(predatorArray[leftNumCard]);
            binding.tvLeftNumber.setBackgroundResource(herbivorousArray[rightNumCard]);
            binding.tvRightNumberText.setText(predatorTextArray[leftNumCard]);
            binding.tvLeftNumberText.setText(herbivorousTextArray[rightNumCard]);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Level3.this, GameLevels.class);
        startActivity(intent);
    }

}