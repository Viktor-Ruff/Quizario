package com.ruff.game_lesson_1;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Viktor-Ruff
 * Date: 30.08.2023
 * Time: 13:55
 */
public class MyMediaPlayer {

    private Context context;
    private int path;

    private MediaPlayer mPlayer;

    public MyMediaPlayer(Context context, int path) {
        this.context = context;
        this.path = path;
        mPlayer = MediaPlayer.create(context, path);
    }

    public void stopPlay() {
        mPlayer.stop();

        try {
            mPlayer.prepare();
            mPlayer.seekTo(0);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void play() {
        mPlayer.start();
    }

    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    public void pause() {
        mPlayer.pause();
    }

}
