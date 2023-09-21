package com.ruff.game_lesson_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.ruff.game_lesson_1.databinding.ActivityMainBinding;

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

        Window w = getWindow();
        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //Скрываем нижнюю панель навигации.
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); //Появляется поверх игры и исчезает.


        //click handling button start
        binding.btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MainActivity.this, GameLevels.class);
                    startActivity(intent);

                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            }
        });
    }



    //click handling system button back
    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(this, "Click again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();

    }
}