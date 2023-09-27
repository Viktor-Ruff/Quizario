package com.ruff.game_lesson_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ruff.game_lesson_1.databinding.ActivityFinishBinding;
import com.ruff.game_lesson_1.databinding.ActivityMainBinding;

public class FinishActivity extends AppCompatActivity {

    public static final String MY_LOG = "MY_LOG";
    private ActivityFinishBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFinishBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(FinishActivity.this, GameLevels.class);
                    startActivity(intent);
                    finish();

                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            }
        });
    }
}