package edu.umsl.yerby.simondroid.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.umsl.yerby.simondroid.R;

public class SimonMainActivity extends AppCompatActivity {

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

        mHighScoreTextView = (TextView)findViewById(R.id.highscore_text_view);
        mHighScoreTextView.setText(getString(R.string.highscore_text_view,mHighScore));

    }

    public void startGame_OnClick( View view ) {
       Intent gameBoardIntent = new Intent(SimonMainActivity.this,GameBoardActivity.class);
        startActivity(gameBoardIntent);

    }


}
