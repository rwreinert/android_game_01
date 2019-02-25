package com.example.robert.gametemplate;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PlayGame extends AppCompatActivity {

    private final int BONUS_LIFE_INTERVAL = 10, LEVEL_INTERVAL = 100, DEFAULT_LEVEL_COUNTDOWN = 5000;


    private Button mButton1, mButton2, mButton3, mButton4;
    private TextView mScoreView, mLivesView, mLevelView, mBonusText;
    private ImageView mGuy1, mGuy2, mGuy3, mGuy4;
    private ImageView mBomb;
    private ProgressBar mFuse;
    private CountDownTimer mTimer, mRoundTimer, mBonusTextTimer;
    private int currLevel, lives, score;
    private int target, bonusLifeNum, bonusComboNum, comboCount, highestCombo;
    private LinearLayout controlPanel, bonusLayout;
    private SoundPlayer soundPlayer;
    private boolean repeatLevel; // If the player fails the current level. Prevents timer dec..
    private int levelCountdown;


    // Set Up Methods: ========================================================================

    private View.OnClickListener setClickListener(final int num) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleResponse(num);
            }
        };
    }

    // The reset game timer.
    private CountDownTimer setCountDown() {
        return new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                soundPlayer.playCountDown();

            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(), "GO!", Toast.LENGTH_SHORT).show();
                soundPlayer.playStart();
                startScreen(1);
            }
        };
    }

    // Sets up the level countdown timer as the bomb fuse is lit
    private CountDownTimer setLevelTimer(int level, final int time, final int interval) {
        return new CountDownTimer(time, LEVEL_INTERVAL) {

            @Override
            public void onTick(long millisUntilFinished) {
                // Update the progress bar
                int amount = mFuse.getMax() / (time / LEVEL_INTERVAL);
                mFuse.setProgress(mFuse.getProgress() + (amount) + 1, true);
            }

            @Override
            public void onFinish() {
                soundPlayer.playBomb();
                failLevel();
            }
        };
    }

    // The view for onscreen text.
    private CountDownTimer setBonusView() {
        return new CountDownTimer(2000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                mBonusText.setVisibility(View.INVISIBLE);
            }
        };
    }


    // GAME METHODS =========================================================================

    private void resetComboStreak() {
        comboCount = 0;
        bonusLifeNum = BONUS_LIFE_INTERVAL;
        bonusComboNum = 5;
    }

    // When the bomb goes off or player hits wrong guy
    private void failLevel() {
        Toast.makeText(getApplicationContext(), "Ouch! The correct one is: " + target, Toast.LENGTH_LONG).show();
        lives--;
        repeatLevel = true;

        // Cancel any timers
        mRoundTimer.cancel();
//        mBonusTextTimer.cancel();

        // Reset level or exit to menu
        if (lives >= 0) {
            resetComboStreak();
            startScreen(currLevel);
        } else {
            soundPlayer.playGameOver();

            // Create a Dialog box
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder
                    .setCancelable(false)
                    .setMessage("You died.")
                    .setTitle("KABOOM!")
                    .setIcon(R.drawable.badguy)
                    .setNegativeButton("Danggg...", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            resetScreen();
                        }
                    });


            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    // Deals with handling the user input of up to 4 buttons
    private void handleResponse(int num) {
        if (num == target) {
//            Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT).show();
            repeatLevel = false;
            currLevel++;
            score++;
            comboCount++;

            // Set the new highest combo!
            if (comboCount > highestCombo) {
                highestCombo = comboCount;
                mBonusTextTimer.cancel();
                mBonusText.setText("NEW HIGH COMBO: " + highestCombo + "!");
                mBonusText.setVisibility(View.VISIBLE);
                mBonusTextTimer.start();
            }

            // Check for bonus points
            if (comboCount == bonusComboNum) {
                mBonusTextTimer.cancel();
                mBonusText.setText("BONUS REWARDED!");
                mBonusText.setVisibility(View.VISIBLE);
                mBonusTextTimer.start();

                score += bonusComboNum;
                bonusComboNum *= 2;
            }
            // Check for bonus lives
            if (comboCount == bonusLifeNum) {
                mBonusTextTimer.cancel();
                mBonusText.setText("EXTRA LIFE!");
                mBonusText.setVisibility(View.VISIBLE);
                mBonusTextTimer.start();
                lives += 1;
                bonusLifeNum += BONUS_LIFE_INTERVAL;
            }

            soundPlayer.playWin();
            mRoundTimer.cancel();
            startScreen(currLevel);

        } else {
            soundPlayer.playLose();
            failLevel();
        }
    }

    private void generateTarget(int max) {
        target = (int) (Math.random() * max) + 1;

//        mGuy1.setColorFilter(Color.TRANSPARENT);
//        mGuy2.setColorFilter(Color.TRANSPARENT);
//        mGuy3.setColorFilter(Color.TRANSPARENT);
//        mGuy4.setColorFilter(Color.TRANSPARENT);

        // Reset the image resource for the people.
        mGuy1.setImageResource(R.drawable.police);
        mGuy2.setImageResource(R.drawable.police);
        mGuy3.setImageResource(R.drawable.police);
        mGuy4.setImageResource(R.drawable.police);

        if (target == 1) {
            mGuy1.setImageResource(R.drawable.badguy);
        } else if (target == 2) {
            mGuy2.setImageResource(R.drawable.badguy);
        } else if (target == 3) {
            mGuy3.setImageResource(R.drawable.badguy);
        } else {
            mGuy4.setImageResource(R.drawable.badguy);
        }

    }

    // Resets the screen for a new game.
    private void resetScreen() {
        lives = 3;
        score = 0;
        currLevel = 1;
        repeatLevel = false;
        mFuse.setMax(100);
        resetComboStreak();
        levelCountdown = 5000;
        mRoundTimer = setLevelTimer(1, levelCountdown, LEVEL_INTERVAL);

        controlPanel.setVisibility(View.INVISIBLE);
        mBonusText.setVisibility(View.INVISIBLE);
        mButton3.setVisibility(View.INVISIBLE);
        mButton4.setVisibility(View.INVISIBLE);

        mGuy1.setVisibility(View.INVISIBLE);
        mGuy2.setVisibility(View.INVISIBLE);
        mGuy3.setVisibility(View.INVISIBLE);
        mGuy4.setVisibility(View.INVISIBLE);

        mBomb.setVisibility(View.INVISIBLE);
        mFuse.setVisibility(View.INVISIBLE);

        mScoreView.setText("Score: " + score);
        mLivesView.setText("Lives: " + lives);
        mLevelView.setText("Level: " + currLevel);

        // Start the 3 second count down for playing.
        mTimer.start();
    }

    // Sets up activity components
    private void startScreen(int level) {

        if (controlPanel.getVisibility() == View.INVISIBLE)
            controlPanel.setVisibility(View.VISIBLE);
        if (mFuse.getVisibility() == View.INVISIBLE)
            mFuse.setVisibility(View.VISIBLE);
        if (mBomb.getVisibility() == View.INVISIBLE)
            mBomb.setVisibility(View.VISIBLE);
        if (mGuy1.getVisibility() == View.INVISIBLE)
            mGuy1.setVisibility(View.VISIBLE);
        if (mGuy2.getVisibility() == View.INVISIBLE)
            mGuy2.setVisibility(View.VISIBLE);

        // Default base for ALL levels.
        mScoreView.setText("Score: " + score);
        mLivesView.setText("Lives: " + lives);
        mLevelView.setText("Level: " + currLevel);

        mFuse.setProgress(0);

        //Add a third person
        if (level > 5) {
            if (mButton3.getVisibility() == View.INVISIBLE)
                mButton3.setVisibility(View.VISIBLE);
            if (mGuy3.getVisibility() == View.INVISIBLE)
                mGuy3.setVisibility(View.VISIBLE);
        }
        // Add a forth person
        if (level > 10) {
            if (mButton4.getVisibility() == View.INVISIBLE)
                mButton4.setVisibility(View.VISIBLE);
            if (mGuy4.getVisibility() == View.INVISIBLE)
                mGuy4.setVisibility(View.VISIBLE);
        }
        // Start!
        startRound(level);
    }

    // Sets up game variables for each round
    private void startRound(int level) {
        // Decrease fuse time:
        if (level % 10 == 0 && levelCountdown > 500 && !repeatLevel) {
            Toast.makeText(this, "Fuse shortened!", Toast.LENGTH_SHORT).show();
            levelCountdown -= 500;
            mRoundTimer = setLevelTimer(level, levelCountdown, LEVEL_INTERVAL);
        }

        if (level > 10)
            generateTarget(4);
        else if (level > 5)
            generateTarget(3);
        else
            generateTarget(2);

        mRoundTimer.start();
    }


    // ==========================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the variables
        levelCountdown = 5000;
        mTimer = setCountDown();
        mRoundTimer = setLevelTimer(1, levelCountdown, LEVEL_INTERVAL);
        mBonusTextTimer = setBonusView();
        soundPlayer = new SoundPlayer(getApplicationContext());

        setContentView(R.layout.activity_play_game);

        // Fetch the buttons and components
        mButton1 = findViewById(R.id.b1);
        mButton2 = findViewById(R.id.b2);
        mButton3 = findViewById(R.id.b3);
        mButton4 = findViewById(R.id.b4);
        controlPanel = findViewById(R.id.controlPanel);
        bonusLayout = findViewById(R.id.bonusLayout);
        mLevelView = findViewById(R.id.levelView);
        mLivesView = findViewById(R.id.livesView);
        mScoreView = findViewById(R.id.scoreView);
        mBonusText = findViewById(R.id.bonusCountView);
        mGuy1 = findViewById(R.id.guy1);
        mGuy2 = findViewById(R.id.guy2);
        mGuy3 = findViewById(R.id.guy3);
        mGuy4 = findViewById(R.id.guy4);
        mBomb = findViewById(R.id.bombView);
        mFuse = findViewById(R.id.progressBar);


        // Set the onclick listeners
        mButton1.setOnClickListener(setClickListener(1));
        mButton2.setOnClickListener(setClickListener(2));
        mButton3.setOnClickListener(setClickListener(3));
        mButton4.setOnClickListener(setClickListener(4));

        mGuy1.setOnClickListener(setClickListener(1));
        mGuy2.setOnClickListener(setClickListener(2));
        mGuy3.setOnClickListener(setClickListener(3));
        mGuy4.setOnClickListener(setClickListener(4));

        // Begin the game!
        resetScreen();
    }


    @Override
    public void onPause() {
        mRoundTimer.cancel();
        mTimer.cancel();
        // Need to free up soundplayer
        soundPlayer.release();
        soundPlayer = null;
        // Call super method
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        soundPlayer = new SoundPlayer(getApplicationContext());
    }

    @Override
    public void onStop() {
        mRoundTimer.cancel();
        mTimer.cancel();
        super.onStop();
    }
}
