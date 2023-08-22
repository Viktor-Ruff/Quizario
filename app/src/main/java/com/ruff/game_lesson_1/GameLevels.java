package com.ruff.game_lesson_1;

import static com.ruff.game_lesson_1.MainActivity.MY_LOG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class GameLevels extends AppCompatActivity {

    Button btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_levels);

        btBack = (Button) findViewById(R.id.bt_back);

        //click handling button back
        btBack.setOnClickListener(new View.OnClickListener() {
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

}