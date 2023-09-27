package com.ruff.game_lesson_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.ruff.game_lesson_1.databinding.ActivityMainBinding;
import com.ruff.game_lesson_1.levels.Level1;
import com.ruff.game_lesson_1.levels.Level2;

public class MainActivity extends AppCompatActivity {

    public static final String MY_LOG = "MY_LOG";

    private ActivityMainBinding binding;
    Toast backToast;

    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Скрываем нижнюю панель навигации.
        Window w = getWindow();
        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); //Появляется поверх игры и исчезает.

        //Запрещаем ночную тему.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        //click handling button start
        binding.btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MainActivity.this, GameLevels.class);
                    startActivity(intent);
                    finish();

                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            }
        });

        binding.ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialog();
            }
        });
    }


    private void initDialog() {

        Dialog dialogEnd = new Dialog(this);
        dialogEnd.setContentView(R.layout.end_dialog); //выбор макета для диалога

        Window w = dialogEnd.getWindow();
        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //Скрываем нижнюю панель навигации.
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); //Появляется поверх игры и исчезает.

        ConstraintLayout constraintLayout = dialogEnd.findViewById(R.id.my_end_dialog_constraint);
        constraintLayout.setBackgroundResource(R.drawable.im_back_dialog_level1); //выбор заднего фона для главного контейнера диалога
        dialogEnd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //затемнение заднего фона диалога
        TextView tvTextDialogEnd = dialogEnd.findViewById(R.id.textView);
        tvTextDialogEnd.setText(getResources().getString(R.string.gameplay_text));
        dialogEnd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT); //растягивания диалога на экране
        dialogEnd.setCancelable(false); //отключение системной кнопки назад
        dialogEnd.show(); //отображение диалога

        MaterialButton close = dialogEnd.findViewById(R.id.bt_close_dialog);
        MaterialButton _continue = dialogEnd.findViewById(R.id.bt_continue);

        //обработка кнопки закрыть (крестик) - начало.
        close.setOnClickListener(v -> {
            dialogEnd.cancel();
        });
        //обработка кнопки закрыть (крестик) - конец.


        //обработка кнопки продолжить - начало
        _continue.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GameLevels.class);
            startActivity(intent);
            finish();
            dialogEnd.cancel();
        });
        //обработка кнопки продолжить - конец

    }


    //click handling system button back
    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            finish();
            return;
        } else {
            backToast = Toast.makeText(this, "Click again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();

    }
}