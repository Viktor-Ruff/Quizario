package com.ruff.game_lesson_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ruff.game_lesson_1.databinding.UniversalBinding;

public class Level1 extends AppCompatActivity {

    private UniversalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UniversalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvLeftNumber.setClipToOutline(true);
        binding.tvRightNumber.setClipToOutline(true);
    }
}