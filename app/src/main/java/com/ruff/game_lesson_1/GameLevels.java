package com.ruff.game_lesson_1;

import static com.ruff.game_lesson_1.MainActivity.MY_LOG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ruff.game_lesson_1.databinding.ActivityGameLevelsBinding;

public class GameLevels extends AppCompatActivity {

    private ActivityGameLevelsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameLevelsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        levelListeners();

        //click handling button back
        binding.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onBackPressed();
                } catch (Exception e) {
                    Log.d(MY_LOG, e.getMessage());
                }
            }
        });

    }

    private void levelListeners() {


        //enter to level 1
        binding.tvLevel1.setOnClickListener(v -> {

            try {
                Intent intent = new Intent(GameLevels.this, Level1.class);
                startActivity(intent);
            } catch (Exception e) {
                Log.d(MY_LOG, e.getMessage());
            }
        });
    }

}

