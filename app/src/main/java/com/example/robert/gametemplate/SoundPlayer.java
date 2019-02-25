package com.example.robert.gametemplate;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer {

    private static SoundPool soundPool;
    private AudioAttributes audioAttributes;
    private static int win1, win2, lose1, lose2, bomb1, bomb2, gameOver, start, countDown;


    //Default constructor
    public SoundPlayer(Context context) {
        audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(audioAttributes)
                .build();

        // Load the sounds
        win1 = soundPool.load(context, R.raw.win, 1);
        win2 = soundPool.load(context, R.raw.win2, 1);
        lose1 = soundPool.load(context, R.raw.lose, 1);
        lose2 = soundPool.load(context, R.raw.lose2, 1);
        bomb1 = soundPool.load(context, R.raw.bomb, 1);
        bomb2 = soundPool.load(context, R.raw.bomb2, 1);

        gameOver = soundPool.load(context, R.raw.gameover, 2);
        start = soundPool.load(context, R.raw.go, 1);
        countDown = soundPool.load(context, R.raw.countdown, 1);
    }


    // Class Functions
    public void release() {
        soundPool.release();
    }

    public void unload() {
        soundPool.unload(win1);
    }

    public void stopAudio() {

    }


    // Play audio methods
    public void playWin() {
        int soundNum = (int) (Math.random() * 2) + 1;

        if (soundNum == 1)
            soundPool.play(win1, 1.0f, 1.0f, 1, 0, 1.0f);
        else
            soundPool.play(win2, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playLose() {
        int soundNum = (int) (Math.random() * 2) + 1;

        if (soundNum == 1)
            soundPool.play(lose1, 1.0f, 1.0f, 1, 0, 1.0f);
        else
            soundPool.play(lose2, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playBomb() {
        int soundNum = (int) (Math.random() * 2) + 1;

        if (soundNum == 1)
            soundPool.play(bomb1, 1.0f, 1.0f, 1, 0, 1.0f);
        else
            soundPool.play(bomb2, 1.0f, 1.0f, 1, 0, 1.0f);
    }


    public void playGameOver() {
        soundPool.play(gameOver, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playCountDown() {
        soundPool.play(countDown, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playStart() {
        soundPool.play(start, 1.0f, 1.0f, 1, 0, 1.0f);
    }

}
