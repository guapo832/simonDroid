package edu.umsl.yerby.simondroid.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.umsl.yerby.simondroid.R;

public class SimonMainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_GAME_BOARD = 0;
    private int mHighScore;
    private TextView mHighScoreTextView;

    private static final String KEY_HIGH_SCORE = "edu.umsl.gyerby.simon.highscoreKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simon_main);
        if(savedInstanceState !=null){
            mHighScore = savedInstanceState.getInt(KEY_HIGH_SCORE,0);
        }

       setHighScoreText();

    }

    private void setHighScoreText() {
        mHighScoreTextView = (TextView)findViewById(R.id.highscore_text_view);
        mHighScoreTextView.setText(getString(R.string.highscore_text_view,mHighScore));
    }


    // @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(resultCode != Activity.RESULT_OK){
           return;
       }

        if(requestCode == REQUEST_CODE_GAME_BOARD){
            mHighScore = GameBoardActivity.getHighScore(data);
            setHighScoreText();

        }

    }



    public void startGame_OnClick( View view ) {
       Intent gameBoardIntent = new Intent(SimonMainActivity.this,GameBoardActivity.class);
       startActivityForResult(gameBoardIntent,REQUEST_CODE_GAME_BOARD);

    }


}
