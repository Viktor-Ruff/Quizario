package com.ruff.game_lesson_1;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Viktor-Ruff
 * Date: 31.08.2023
 * Time: 12:39
 */
public class LivesSingleton {


    private static final String LIVE_FILE = "LIVE_FILE";
    private static final String LIVE_KEY = "LIVE_KEY";
    SharedPreferences getLivePref;

    private int currentLives;
    private int maxLives;

    private boolean endlessLives;

    private static LivesSingleton instance;

    private LivesSingleton(Context context) {
        getLivePref = context.getSharedPreferences(LIVE_FILE, Context.MODE_PRIVATE);
        maxLives = 10;
        currentLives = getLivePref.getInt(LIVE_KEY, maxLives);
        endlessLives = false;
    }

    public static LivesSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new LivesSingleton(context);
        }

        return instance;
    }

    public int getCurrentLives() {
        return currentLives;
    }

    public void setCurrentLives(int currentLives) {
        this.currentLives = currentLives;
    }

    public int getMaxLives() {
        return maxLives;
    }

    public boolean isEndlessLives() {
        return endlessLives;
    }

    public void setEndlessLives(boolean endlessLives) {
        this.endlessLives = endlessLives;
    }
}
