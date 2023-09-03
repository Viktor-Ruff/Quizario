package com.ruff.game_lesson_1;

/**
 * Created by Viktor-Ruff
 * Date: 31.08.2023
 * Time: 12:39
 */
public class LivesSingleton {

    private int currentLives;
    private int maxLives;

    private static LivesSingleton instance;

    private LivesSingleton() {
        maxLives = 10;
        currentLives = maxLives;
    }

    public static LivesSingleton getInstance() {
        if (instance == null) {
            instance = new LivesSingleton();
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
}
